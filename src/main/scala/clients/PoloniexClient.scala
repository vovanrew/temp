package clients

import akka.actor.{Actor, ActorLogging, Props}
import info.bitrich.xchangestream.core.{StreamingExchange, StreamingExchangeFactory, StreamingMarketDataService}
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange
import org.knowm.xchange.{Exchange, ExchangeFactory}
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.marketdata.OrderBook
import org.knowm.xchange.poloniex.PoloniexExchange
import org.knowm.xchange.service.marketdata.MarketDataService

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

class PoloniexClient extends Actor with ActorLogging {
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
      log.info("POLONIX ORDER BOOK: {}\n" ,marketDataService.getOrderBook(pair, depth.asInstanceOf[Object]))


    case GetTickers(currencyPair) =>
      streamingMarketDataService.getTicker(currencyPair).subscribe(ticker =>
        log.info("POLONIX TICKER: {}\n", ticker.toString),
        throwable => log.error("ERROR in getting ticker: ", throwable))


    case GetTrades(currencyPair) =>
      streamingMarketDataService.getTrades(currencyPair).subscribe(trade =>
      log.info("POLONIX TRADE: {}\n", trade.toString),
        throwable => log.error("ERROR in getting trade: ", throwable))
  }
}

