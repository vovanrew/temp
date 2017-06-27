import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import clients.xchange.PoloniexClient
import common.PoloniexCurrencies
import org.knowm.xchange.currency.CurrencyPair

import scala.concurrent.duration._


object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val poloniex = system.actorOf(PoloniexClient.props)

//  poloniex ! PoloniexClient.Connect

  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBook(PoloniexCurrencies.BTC_USDT, 5))
//  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBook(PoloniexCurrencies.BTC_ETH, 5))
//  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBook(PoloniexCurrencies.BTC_XMR, 5))

//  poloniex ! PoloniexClient.GetTickers(PoloniexCurrencies.BTC_ETH)
//  poloniex ! PoloniexClient.GetTickers(PoloniexCurrencies.BTC_XMR)
//
//  poloniex ! PoloniexClient.GetTrades(PoloniexCurrencies.BTC_ETH)
//  poloniex ! PoloniexClient.GetTrades(PoloniexCurrencies.BTC_XMR)
//
}
