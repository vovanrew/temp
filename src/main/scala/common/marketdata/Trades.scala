package common.marketdata

import org.knowm.xchange.dto.marketdata.Trades.TradeSortType


case class Trades(trades: List[Trade],
                  lastID: Long,
                  tradeSortType: TradeSortType) extends MarketData
