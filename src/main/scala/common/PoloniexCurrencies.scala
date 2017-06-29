package common

import org.knowm.xchange.currency.{Currency, CurrencyPair}


object PoloniexCurrencies {

  val USDT_BTC: CurrencyPair = new CurrencyPair("USDT", "BTC")
  val ETH_BTC: CurrencyPair = new CurrencyPair(Currency.ETH, Currency.BTC)
  val XMR_BTC: CurrencyPair = new CurrencyPair(Currency.XMR, Currency.BTC)

  val BTC_USDT: CurrencyPair = new CurrencyPair("BTC", "USDT")
  val BTC_ETH: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.ETH)
  val BTC_XMR: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.XMR)

  def btcFirst(currencyPair: String): String = {

    if(currencyPair.startsWith("BTC")) currencyPair
    else {
      val secondCurrency = currencyPair
          .substring(0, currencyPair.lastIndexOf("/"))

      currencyPair
        .substring(currencyPair.lastIndexOf("/") + 1, currencyPair.size)
        .concat("/" + secondCurrency)
    }
  }
}
