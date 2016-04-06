package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;



/**
 * This  class holds static methods to create and start stock threads
 */
 class StockManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockManager.class);
    private static List<StockThread> stockThreads;
    private static final String stockNamePrefix = "stockThread";
    private static final int STOCK_THREADS_SIZE = 1;

    // This class does nt need to be instantiated
    private StockManager(){

    }

    static {
        stockThreads = new ArrayList<StockThread>();
        for (int i = 0; i < STOCK_THREADS_SIZE; i++) {
            StockThread stockThread = new StockThread(stockNamePrefix);
            stockThreads.add(stockThread);
        }
    }

    static void start() {
        LOGGER.info("Starting stock thread");
        int i = 0;
        for (StockThread stockThread : stockThreads) {
            stockThread.start();
        }
        LOGGER.info("Started stock thread");
    }

    static void stop() {
        for (StockThread stockThread : stockThreads) {
            stockThread.stop();
        }
    }
}
