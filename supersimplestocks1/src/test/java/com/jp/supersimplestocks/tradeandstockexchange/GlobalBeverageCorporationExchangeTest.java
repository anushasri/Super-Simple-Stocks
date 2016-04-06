package com.jp.supersimplestocks.tradeandstockexchange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class GlobalBeverageCorporationExchangeTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalBeverageCorporationExchangeTest.class);


    /**
     * This is a positive test to verify stock price ,dividend yield ,PE ratio and GBCE all share index
     *
     * @throws InterruptedException
     * @throws StockException
     */
    @Test
    public void stockPricesForTradesWithIn15MinShouldAssertTrue() throws InterruptedException, StockException {
        LOGGER.info("Test : Executing stockPricesForTradesWithIn15MinShouldAssertTrue ...");
        TradesQueue.INSTANCE.clear();
        GlobalBeverageExchangeCorporation gbce = GlobalBeverageExchangeCorporation.INSTANCE;
        gbce.resetLatch(new CountDownLatch(1));
        setUpDataWithIn15Min();
        GlobalBeverageCorporationExchangeHelper.startStockManager();
        gbce.waitUntilStocksAreRefreshed();
        GlobalBeverageCorporationExchangeHelper.stopStockManager();
        Map<String, Stock> listedStocksMap = gbce.getListedStocksMap();
        BigDecimal expectedShareIndexProduct = BigDecimal.ONE;
        for (Stock stockActual : listedStocksMap.values()) {
            StockTest stockExpected = stockMap.get(stockActual.getStockSymbol());
            expectedShareIndexProduct = expectedShareIndexProduct.multiply(stockExpected.getStockPrice());

            assertEquals("Stock price for stock :" + stockActual.getStockSymbol() + " is incorrect", stockExpected.getStockPrice(), stockActual.getStockPrice());
            assertEquals("Dividend yield for stock :" + stockActual.getStockSymbol() + " is incorrect", stockExpected.getDividendYield(), stockActual.getDividendYield());
            assertEquals("PE ratio for stock :" + stockActual.getStockSymbol() + " is incorrect", stockExpected.getpERatio(), stockActual.getpERatio());
        }
        double expectedShareIndexDouble = Math.pow(expectedShareIndexProduct.doubleValue(), BigDecimal.ONE.divide(new BigDecimal(StockStatic.values().length)).doubleValue());
        BigDecimal expectedShareIndex = new BigDecimal(expectedShareIndexDouble).setScale(2, RoundingMode.CEILING);
        assertEquals("All share index of GBCE  is incorrect", expectedShareIndex, gbce.getShareIndex());
    }

    /**
     * This  is a negative test to ensure stock is calculated for the last 15 min trades only.
     *
     * @throws InterruptedException
     * @throws StockException
     */
    @Test
    public void stockPricesForTradesBeyond15MinShouldAssertFalse() throws InterruptedException, StockException {
        LOGGER.info("Test : Executing stockPricesForTradesBeyond15MinShouldAssertFalse ...");
        TradesQueue.INSTANCE.clear();
        GlobalBeverageExchangeCorporation gbce = GlobalBeverageExchangeCorporation.INSTANCE;
        gbce.resetLatch(new CountDownLatch(1));
        setUpDataBeyond15Min();
        GlobalBeverageCorporationExchangeHelper.startStockManager();
        gbce.waitUntilStocksAreRefreshed();
        GlobalBeverageCorporationExchangeHelper.stopStockManager();
        Map<String, Stock> listedStocksMap = gbce.getListedStocksMap();
        for (Stock stockActual : listedStocksMap.values()) {
            StockTest stockExpected = stockMap.get(stockActual.getStockSymbol());
            assertNotEquals("Stock price for stock :" + stockActual.getStockSymbol() + " is incorrect", stockExpected.getStockPrice(), stockActual.getStockPrice());
        }
    }

    /**
     * This is a simulation of real application if needed to be invoked by GUI
     * Test runs for approximately 10000 ms posting trades and calculating stock prices at regular intervals
     */
    @Test
    public void testGBCE() {
        LOGGER.info("Test : Executing runGBCE ...");
        TradesQueue.INSTANCE.clear();
        GlobalBeverageCorporationExchangeHelper.startGBCE();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GlobalBeverageCorporationExchangeHelper.stopGBCE();
    }


}
