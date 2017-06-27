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

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object PoloniexClient {

  case object Connect
  case object Disconnect
  case object GetConnectionState

  case class GetOrderBook(pair: CurrencyPair, depth: Int)
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

  val props = new Properties()
  props.put("bootstrap.servers", kafkaHost + ":" + kafkaPort.toString)
  props.put("acks", "all")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "common.serialization.MarketDataSerializer")


  private val kafkaProducer: PoloniexKafkaProducer = new PoloniexKafkaProducer(props)

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


    case GetOrderBook(pair, depth) =>
      val orderBook = marketDataService.getOrderBook(pair, depth.asInstanceOf[Object])
      kafkaProducer.send("ORDERBOOK", pair, OrderBook(orderBook.getTimeStamp,
        orderBook.getAsks.asScala.toList, orderBook.getBids.asScala.toList))


    case GetTickers(currencyPair) =>
      streamingMarketDataService.getTicker(currencyPair).subscribe(ticker =>
        kafkaProducer.send("TICKER", ticker.getCurrencyPair,
          Ticker(ticker.getCurrencyPair,
            ticker.getLast,
            ticker.getBid,
            ticker.getAsk,
            ticker.getHigh,
            ticker.getLow,
            ticker.getVwap,
            ticker.getVolume,
            ticker.getTimestamp)),
        throwable => log.error("ERROR in getting ticker: ", throwable))


    case GetTrades(currencyPair) =>
      streamingMarketDataService.getTrades(currencyPair).subscribe(trade =>
      kafkaProducer.send("TRADE", trade.getCurrencyPair,
        Trade(trade.getType,
          trade.getTradableAmount,
          trade.getCurrencyPair,
          trade.getPrice,
          trade.getTimestamp,
          trade.getId)),
        throwable => log.error("ERROR in getting trade: ", throwable))
  }
}

