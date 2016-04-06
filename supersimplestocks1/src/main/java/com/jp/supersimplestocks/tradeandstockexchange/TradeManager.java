package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class TradeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeManager.class);
    private static List<Trader> traderThreads;
    private static final String traderNamePrefix = "trader";
    public static final int TRADER_THREADS_SIZE = 5;


    static {
        traderThreads = new ArrayList<Trader>();
        for (int i = 0; i < TRADER_THREADS_SIZE; i++) {
            Trader trader = new Trader(traderNamePrefix);
            traderThreads.add(trader);
        }
    }

    /**
     * This method starts the trader threads
     */
    static void start() {
        LOGGER.info("Starting trade thread");
        for (Trader trader : traderThreads) {
            trader.start();
        }
        LOGGER.info("Started trade thread");
    }

    /**
     * This method stops the trader threads
     */
    static void stop() {
        for (Trader trader : traderThreads) {
            trader.stop();
        }
    }
}
