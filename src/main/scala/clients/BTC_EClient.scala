package clients


import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import org.knowm.xchange.btce.v3.BTCEExchange
import org.knowm.xchange.{Exchange, ExchangeFactory}
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.service.marketdata.MarketDataService
import scala.util.{Failure, Success}


object BTC_EClient {

  case object GetConnectionState

  case class GetOrderBook(pair: CurrencyPair, depth: Int)
  case class GetTicker(pair: CurrencyPair)
  case class GetTrage(pair: CurrencyPair)

  def props: Props = Props(new BTC_EClient)
}


class BTC_EClient extends Actor with ActorLogging {

  import context.dispatcher
  import BTC_EClient._

  val exchange: Exchange = ExchangeFactory
  .INSTANCE
  .createExchange((new BTCEExchange).getClass.getName)

  val marketDataService: MarketDataService = exchange.getMarketDataService

  var connectionStatus: Boolean = false

  final implicit val materializer: ActorMaterializer =
  ActorMaterializer(ActorMaterializerSettings(context.system))

  override def receive: Receive = {

    case GetConnectionState =>

      Http(context.system).singleRequest(HttpRequest(uri = "https://btc-e.com/api/3/ticker/btc_usd-btc_rur")).onComplete {
        case Success(responce) =>
          connectionStatus = true
          log.info("Connection to BTCE is success")
          sender() ! connectionStatus

        case Failure(exception) =>
          connectionStatus = false
          log.error("Connection to BTCE is failed")
          sender() ! connectionStatus
      }


    case GetOrderBook(pair, depth) =>
      val orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_USD, depth.asInstanceOf[Object])

      log.info("BTCE ORDER BOOK: {}\n", orderBook)


    case GetTicker(pair) =>
      val ticker = marketDataService.getTicker(pair)

      log.info("BTCE TICKER: {}\n", ticker)


    case GetTrage(pair) =>
      val trades = marketDataService.getTrades(CurrencyPair.BTC_EUR)

      log.info("BTCE TRADES: {}\n", trades)

  }
}
