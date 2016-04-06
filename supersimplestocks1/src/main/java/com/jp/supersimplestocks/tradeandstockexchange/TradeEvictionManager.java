package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class TradeEvictionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeEvictionManager.class);
    private static List<TradeEvictionThread> tradeEvictionThreads;
    private static final String tradeEvictionThreadPrefix = "evictionThread";
    public static final int TRADE_EVICTION_THREADS_SIZE = 1;


    static {
        tradeEvictionThreads = new ArrayList<TradeEvictionThread>();
        for (int i = 0; i < TRADE_EVICTION_THREADS_SIZE; i++) {
            TradeEvictionThread tradeEvictionThread = new TradeEvictionThread(tradeEvictionThreadPrefix);
            tradeEvictionThreads.add(tradeEvictionThread);
        }
    }

    static void start() {
        LOGGER.info("Starting eviction thread");
        for (TradeEvictionThread tradeEvictionThread : tradeEvictionThreads) {
            tradeEvictionThread.start();
        }
        LOGGER.info("Started eviction thread");
    }

    static void stop() {
        for (TradeEvictionThread tradeEvictionThread : tradeEvictionThreads) {
            tradeEvictionThread.stop();
        }
    }
}
