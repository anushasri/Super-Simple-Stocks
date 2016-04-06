package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;


class StockThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockThread.class);
    public static final String WORKERNAME = "workername";
    private String stockThreadName;
    private Thread stockThread;

    StockThread(String name) {
        this.stockThreadName = name;
    }

    /**
     * This method starts the stock thread
     */
    final void start() {

        stockThread = new Thread(stockThreadName) {

            @Override
            public void run() {
                MDC.put(WORKERNAME, stockThreadName);
                while (!Thread.interrupted()) {
                    Map<String, TradesHolder> stocksMap;
                    String errMessage = null;
                    BigDecimal stockPriceProduct = BigDecimal.ONE;
                    synchronized (TradesQueue.INSTANCE) {
                        GlobalBeverageExchangeCorporation gbce = GlobalBeverageExchangeCorporation.INSTANCE;
                        LOGGER.info("Got hold of the reading lock..........");
                        LOGGER.info(String.format("%-30s", "-------------------------------------------------"));
                        LOGGER.info(String.format("%-5s %-8s %-8s %-8s", "Symbol", "StockPrice", "DividendYield", "PE Ratio"));
                        LOGGER.info(String.format("%-30s", "-------------------------------------------------"));
                        long currentTimeStampMilliSeconds = Calendar.getInstance().getTime().getTime();
                        Timestamp currentTimestamp = new Timestamp(currentTimeStampMilliSeconds);
                        stocksMap = TradesQueue.INSTANCE.getStockPriceFromTrades(currentTimestamp);
                        Map<String, Stock> listedStocksMap = gbce.getListedStocksMap();
                        for (Map.Entry<String, TradesHolder> entry : stocksMap.entrySet()) {

                            Stock stock = listedStocksMap.get(entry.getKey());
                            BigDecimal stockPrice = null;
                            try {
                                stockPrice = entry.getValue().getStockPrice();
                                stock.setStockPrice(stockPrice);
                            } catch (StockException e) {
                                errMessage = e.getMessage();
                            } finally {
                                String format = ".2f";
                                if (null == stock.getpERatio()) {
                                    format = "s";
                                }
                                LOGGER.info(String.format("%-5s %-8.2f %-8.2f %-8" + format, stock.getStockSymbol(), stockPrice, stock.getDividendYield(), stock.getpERatio() == null ? "Na" : stock.getpERatio()));
                            }
                            stockPriceProduct = stockPriceProduct.multiply(stockPrice);
                        }
                        if (null != errMessage) {
                            LOGGER.error(errMessage);
                        }

                        double shareIndex = Math.pow(stockPriceProduct.doubleValue(), BigDecimal.ONE.divide(new BigDecimal(StockStatic.values().length)).doubleValue());
                        BigDecimal bigDecimalShareIndex = new BigDecimal(shareIndex).setScale(2, RoundingMode.CEILING);
                        gbce.setShareIndex(bigDecimalShareIndex);
                        gbce.setGbceRefreshed(true);
                        LOGGER.info("GBCE all share index --> " + bigDecimalShareIndex.doubleValue());
                        LOGGER.info(String.format("%-30s", "---------------------------------------------------------------"));
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        stockThread.setPriority(5);
        stockThread.start();
    }


    /**
     * This method interrupts the threads for  certain attempts and
     * then throws runtime exception
     */
    final void stop() {
        LOGGER.info("Stopping stockThread ...");
        int attempts = 0;
        while (null != stockThread && stockThread.isAlive()) {
            if (attempts > 0) {
                LOGGER.info("Could not interrupt or join  the stockThread so attempting again");
            }
            stockThread.interrupt();
            try {
                stockThread.join(1000);
            } catch (InterruptedException ex) {
                LOGGER.error("Error shutting down stockThread");
                throw new RuntimeException("Error shutting down stockThread", ex);
            }
            attempts++;
            if (attempts == 5) {
                LOGGER.error("could nt interrupt or join the stockThread after 5 attemps");
                throw new RuntimeException("could nt interrupt or join the stockThread after 5 attemps");
            }
        }
        LOGGER.info("Stopped stockThread thread");
    }
}
