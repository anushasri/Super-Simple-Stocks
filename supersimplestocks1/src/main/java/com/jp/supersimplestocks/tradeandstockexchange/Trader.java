package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;


public class Trader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Trader.class);
    private String traderName;
    private Thread trader;

    Trader(String name) {
        this.traderName = name;
    }

    /**
     * This method starts the trader thread
     */
    final void start() {

        trader = new Thread(traderName) {

            @Override
            public void run() {
                MDC.put("workername", traderName);
                while (!Thread.interrupted()) {

                    int stockRandomInt = new Random().nextInt(5);
                    StockStatic stock = StockStatic.values()[stockRandomInt];
                    int sharesQuantityRandomInt = new Random().nextInt(100) + 1;
                    int tradeIndicatorRandomInt = new Random().nextInt(2);
                    TradeIndicator tradeIndicator = TradeIndicator.values()[tradeIndicatorRandomInt];
                    int sharePriceRandomInt = new Random().nextInt(2) + 1;
                    BigDecimal randomSharePrice = stock.getParValue().multiply(new BigDecimal(sharePriceRandomInt));

                    Trade trade = new Trade(new Timestamp(new Date().getTime()), new BigDecimal(sharesQuantityRandomInt), tradeIndicator, randomSharePrice, stock.name());
                    synchronized (TradesQueue.INSTANCE) {
                        TradesQueue.INSTANCE.recordTrade(trade);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        trader.setPriority(5);
        trader.start();
    }

    /**
     * This method stops the trader thread
     */
    final void stop() {
        LOGGER.info("Stopping trader thread...");
        int attempts = 0;
        while (null != trader && trader.isAlive()) {
            if (attempts > 0) {
                LOGGER.info("Could not interrupt or join  the trader thread so attempting again");
            }
            trader.interrupt();
            try {
                trader.join(1000);
            } catch (InterruptedException ex) {
                LOGGER.error("Error shutting down trader thread");
                throw new RuntimeException("Error shutting down trader thread", ex);
            }
            attempts++;
            if (attempts == 5) {
                LOGGER.error("could nt interrupt or join the thread after 5 attemps");
                throw new RuntimeException("could nt interrupt or join the thread after 5 attemps");
            }
        }
        LOGGER.info("Stopped trader thread");
    }
}
