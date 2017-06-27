package common

import org.knowm.xchange.currency.{Currency, CurrencyPair}


object PoloniexCurrencies {

  val BTC_USDT: CurrencyPair = new CurrencyPair("BTC", "USDT")
  val BTC_ETH: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.ETH)
  val BTC_XMR: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.XMR)
}
