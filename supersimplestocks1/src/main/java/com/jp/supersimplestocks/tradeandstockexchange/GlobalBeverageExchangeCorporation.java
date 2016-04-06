package com.jp.supersimplestocks.tradeandstockexchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;



/**
 * This is a thread safe singleton class which holds listed stocks map and share index
 */
public enum GlobalBeverageExchangeCorporation {

    INSTANCE;

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private final Map<String, Stock> listedStocksMap;
    private BigDecimal shareIndex;
    private boolean gbceRefreshed;

    GlobalBeverageExchangeCorporation() {
        Map<String, Stock> stocksListMap = new HashMap<String, Stock>();
        for (StockStatic stockStatic : StockStatic.values()) {
            String stockSymbol = stockStatic.name();
            Stock stock = new Stock(stockSymbol);
            stocksListMap.put(stockSymbol, stock);
        }
        this.listedStocksMap = stocksListMap;
    }

    public void setGbceRefreshed(boolean gbceRefreshed) {
        this.gbceRefreshed = gbceRefreshed;
        countDownLatch.countDown();
    }

    public void waitUntilStocksAreRefreshed() throws InterruptedException {
        countDownLatch.await();
    }
    public void resetLatch(CountDownLatch countDownLatch)  {
        this.countDownLatch = countDownLatch;
    }

    public Map<String, Stock> getListedStocksMap() {
        return listedStocksMap;
    }


    public BigDecimal getShareIndex() {
        return shareIndex;
    }

    public void setShareIndex(BigDecimal shareIndex) {
        this.shareIndex = shareIndex;
    }
}
