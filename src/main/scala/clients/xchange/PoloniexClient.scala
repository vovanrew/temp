package clients.xchange

import java.util.Properties

import akka.actor.{Actor, ActorLogging, Props}
import clients.kafka.PoloniexKafkaProducer
import common.InitConfs
import common.marketdata.{OrderBook, Ticker, Trade}
import info.bitrich.xchangestream.core.{StreamingExchange, StreamingExchangeFactory, StreamingMarketDataService}
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.poloniex.PoloniexExchange
import org.knowm.xchange.service.marketdata.MarketDataService
import org.knowm.xchange.{Exchange, ExchangeFactory}
import common.PoloniexCurrencies._
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object PoloniexClient {

  case object Connect
  case object Disconnect
  case object GetConnectionState

  case class GetOrderBooks(pair: CurrencyPair, depth: Int)
  case class GetTickers(pair: CurrencyPair)
  case class GetTrades(pair: CurrencyPair)

  def props: Props = Props(new PoloniexClient)
}

class PoloniexClient extends Actor with ActorLogging with InitConfs {
  import PoloniexClient._

  val exchange: Exchange = ExchangeFactory
    .INSTANCE
    .createExchange((new PoloniexExchange).getClass.getName)

  val exchangeStreaming: StreamingExchange = StreamingExchangeFactory
    .INSTANCE
    .createExchange((new PoloniexStreamingExchange).getClass.getName)

  val streamingMarketDataService: StreamingMarketDataService = exchangeStreaming.getStreamingMarketDataService

  val marketDataService: MarketDataService = exchange.getMarketDataService

  var connectionStatus: Boolean = false

  val kafkaProducer: PoloniexKafkaProducer = new PoloniexKafkaProducer

  override def receive: Receive = {

    case Connect =>
      Try(exchangeStreaming.connect().blockingAwait()) match {
        case Success(unit) =>
          connectionStatus = true
          log.info("Connection to Poloniex is success")

        case Failure(exception) =>
          connectionStatus = false
          log.info("Connection to Poloniex is success")
      }


    case Disconnect=>
      exchangeStreaming.disconnect()
      connectionStatus = false


    case GetConnectionState =>
      sender() ! connectionStatus


    case GetOrderBooks(currencyPair, depth) =>
      val orderBook = marketDataService.getOrderBook(currencyPair, depth.asInstanceOf[Object])
      if(orderBook.getAsks.isEmpty && orderBook.getBids.isEmpty) Unit
      else {
        kafkaProducer.send("ORDERBOOK", btcFirst(currencyPair.toString), OrderBook(orderBook.getTimeStamp,
          orderBook.getAsks.asScala.toList, orderBook.getBids.asScala.toList))
      }

    case GetTickers(currencyPair) =>
      streamingMarketDataService.getTicker(currencyPair).subscribe(ticker => {

        val vWap: BigDecimal = ticker.getVwap match {
          case null =>
            0
          case value: java.math.BigDecimal =>
            value
        }

        val timeStamp: BigDecimal = ticker.getTimestamp match {
          case null =>
            System.currentTimeMillis()
          case value: java.util.Date =>
            value.getTime
        }

        println("\n\n\n\n\n\n\n\nTICKER:" + btcFirst(currencyPair.toString) + "\n\n\n\n\n\n\n\n")

        kafkaProducer.send("TICKER", btcFirst(ticker.getCurrencyPair.toString),
          Ticker(ticker.getCurrencyPair,
            ticker.getLast,
            ticker.getBid,
            ticker.getAsk,
            ticker.getHigh,
            ticker.getLow,
            vWap,               //DANGEROUS! field had a null value
            ticker.getVolume,
            timeStamp))},       //DANGEROUS! field had a null value
        throwable => log.error("\n\n\n\n\nERROR in getting TICKER: ", throwable))


    case GetTrades(currencyPair) =>
      streamingMarketDataService.getTrades(currencyPair).subscribe(trade => {

        println("\n\n\n\n\n\n\n\nTRADE: " + btcFirst(trade.getCurrencyPair.toString) + "\n\n\n\n\n\n\n\n")

        kafkaProducer.send("TRADE", btcFirst(currencyPair.toString),
        Trade(trade.getType,
          trade.getTradableAmount,
          trade.getCurrencyPair,
          trade.getPrice,
          trade.getTimestamp,
          trade.getId))},
        throwable => log.error("\n\n\n\n\nERROR in getting TRADE: ", throwable))
  }
}