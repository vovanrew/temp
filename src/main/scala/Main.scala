import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import clients.xchange.{BTCEClient, PoloniexClient}
import org.knowm.xchange.currency.CurrencyPair
import scala.concurrent.duration._
import org.apache.kafka.common.serialization.StringSerializer

object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val poloniex = system.actorOf(PoloniexClient.props)
  val btce = system.actorOf(BTCEClient.props)

  poloniex ! PoloniexClient.Connect
  btce ! BTCEClient.GetConnectionState


  btce ! BTCEClient.GetTrages(CurrencyPair.BTC_USD)
  system.scheduler.schedule(0 second, 20 second, btce,BTCEClient.GetOrderBook(CurrencyPair.BTC_USD, 5))
  btce ! BTCEClient.GetTicker(CurrencyPair.BTC_USD)

  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBook(CurrencyPair.ETH_BTC, 5))
  poloniex ! PoloniexClient.GetTickers(CurrencyPair.LTC_BTC)
  poloniex ! PoloniexClient.GetTrades(CurrencyPair.BTC_LTC)
}
