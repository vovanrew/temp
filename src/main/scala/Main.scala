import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import clients.{BTC_EClient, PoloniexClient}
import org.knowm.xchange.currency.CurrencyPair


object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val poloniex = system.actorOf(PoloniexClient.props)
  val btce = system.actorOf(BTC_EClient.props)

  poloniex ! PoloniexClient.Connect
  btce ! BTC_EClient.GetConnectionState

  btce ! BTC_EClient.GetTrage(CurrencyPair.BTC_USD)
  btce ! BTC_EClient.GetOrderBook(CurrencyPair.BTC_USD, 100)
  btce ! BTC_EClient.GetTicker(CurrencyPair.BTC_USD)
  
  poloniex ! PoloniexClient.GetOrderBook(CurrencyPair.BTC_LTC, 100)
  poloniex ! PoloniexClient.GetTickers(CurrencyPair.LTC_BTC)
  poloniex ! PoloniexClient.GetTrades(CurrencyPair.BTC_LTC)
}