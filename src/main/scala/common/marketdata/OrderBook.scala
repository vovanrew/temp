package common.marketdata

import java.util.Date

case class OrderBook(timestamp: Date,
                     asks: List[LimitOrder],
                     bids: List[LimitOrder]) extends MarketData