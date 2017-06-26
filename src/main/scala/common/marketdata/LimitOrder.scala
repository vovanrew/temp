package common.marketdata

import java.math.BigDecimal
import java.util.Date

import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.Order.{OrderStatus, OrderType}


case class LimitOrder(orderType: OrderType,
                      tradableAmount: BigDecimal,
                      currencyPair: CurrencyPair,
                      id: String,
                      timestamp: Date,
                      limitPrice: BigDecimal,
                      averagePrice: BigDecimal,
                      cumulativeAmount: BigDecimal,
                      status: OrderStatus) extends MarketData
