package common

import org.knowm.xchange.currency.{Currency, CurrencyPair}


object PoloniexCurrencies {

  val USDT_BTC: CurrencyPair = new CurrencyPair("USDT", "BTC")
  val ETH_BTC: CurrencyPair = new CurrencyPair(Currency.ETH, Currency.BTC)
  val XMR_BTC: CurrencyPair = new CurrencyPair(Currency.XMR, Currency.BTC)

  val BTC_USDT: CurrencyPair = new CurrencyPair("BTC", "USDT")
  val BTC_ETH: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.ETH)
  val BTC_XMR: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.XMR)

  def btcFirst(currencyPair: CurrencyPair): String = {

    if(currencyPair.toString.startsWith("BTC")) currencyPair.toString
    else {
      val secondCurrency = currencyPair
        .toString.substring(0, currencyPair.toString.lastIndexOf("_"))

      println("\n\n\n\n\n\n\n\n" + currencyPair.toString
        .substring(currencyPair.toString.lastIndexOf("/" + 1), currencyPair.toString.size)
        .concat("/"+secondCurrency) + "\n\n\n\n\n")

      currencyPair.toString
        .substring(currencyPair.toString.lastIndexOf("/" + 1), currencyPair.toString.size)
        .concat("/"+secondCurrency)
    }
  }
}
