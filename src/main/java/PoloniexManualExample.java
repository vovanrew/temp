
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoloniexManualExample {
    private static final Logger LOG = LoggerFactory.getLogger(PoloniexManualExample.class);

    public static void main(String[] args) {
        StreamingExchange exchange = StreamingExchangeFactory.INSTANCE.createExchange(PoloniexStreamingExchange.class.getName());
        exchange.connect().blockingAwait();

//        exchange.getStreamingMarketDataService().getOrderBook(CurrencyPair.BTC_LTC).subscribe(orderBook -> {
//            LOG.info("ASKS: {}\n" + orderBook.getBids()/*.get(0)*/);
//            LOG.info("BIDS: {}\n" + orderBook.getAsks()/*.get(0)*/);
//        }, throwable -> LOG.error("ERROR in getting order book: " + throwable));

        exchange.getStreamingMarketDataService().getTicker(CurrencyPair.BTC_USD).subscribe(ticker -> {
            LOG.info("TICKER: {}", ticker);
        }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

//        exchange.getStreamingMarketDataService().getTrades(CurrencyPair.BTC_LTC).subscribe(trade -> {
//            LOG.info("TRADE: {}", trade);
//        }, throwable -> LOG.error("ERROR in getting trades: ", throwable));

        try {
            Thread.sleep(60000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
