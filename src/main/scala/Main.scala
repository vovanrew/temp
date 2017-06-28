import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import clients.xchange.PoloniexClient
import common.PoloniexCurrencies

import scala.concurrent.duration._


object Main extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val poloniex = system.actorOf(PoloniexClient.props)

  poloniex ! PoloniexClient.Connect

//  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBooks(PoloniexCurrencies.BTC_USDT, 5))
//  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBooks(PoloniexCurrencies.ETH_BTC, 5))
//  system.scheduler.schedule(0 second, 10 second, poloniex, PoloniexClient.GetOrderBooks(PoloniexCurrencies.XMR_BTC, 5))


  poloniex ! PoloniexClient.GetTickers(PoloniexCurrencies.BTC_USDT)
  poloniex ! PoloniexClient.GetTrades(PoloniexCurrencies.USDT_BTC)

  poloniex ! PoloniexClient.GetTickers(PoloniexCurrencies.ETH_BTC)
  poloniex ! PoloniexClient.GetTrades(PoloniexCurrencies.BTC_ETH)

  poloniex ! PoloniexClient.GetTickers(PoloniexCurrencies.XMR_BTC)
  poloniex ! PoloniexClient.GetTrades(PoloniexCurrencies.BTC_XMR)
}