package com.jp.supersimplestocks.tradeandstockexchange;



/**
 * This is  a helper class to start TradeManager,StockManager and Eviction Manager
 * thread pools.
 */
final class GlobalBeverageCorporationExchangeHelper {

    private GlobalBeverageCorporationExchangeHelper() {

    }

    /**
     * This method gets invoked from GUI ,if there is any
     */
    static void startGBCE() {
        startTradeManager();
        startStockManager();
        startEvictionManager();


    }

    /**
     * This method gets invoked from GUI ,if there is any
     */
    static void stopGBCE() {
        stopTradeManager();
        stopStockManager();
        stopEvictionManager();
    }

    private static void startEvictionManager() {
        TradeEvictionManager.start();
    }

    /**
     * This method starts the stock threads
     */
    static void startStockManager() {
        StockManager.start();
    }

    private static void startTradeManager() {
        TradeManager.start();
    }
    private static void stopEvictionManager() {
        TradeEvictionManager.stop();
    }
     static void stopStockManager() {
        StockManager.stop();
    }

    private static void stopTradeManager() {
        TradeManager.stop();
    }
}
