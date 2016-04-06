package com.jp.supersimplestocks.tradeandstockexchange;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;


public class BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    private static final long FIVE_MINUTES_DURATION_IN_MS = 5 * 60 * 1000;
    private static final long EIGHT_MINUTES_DURATION_IN_MS = 8 * 60 * 1000;
    private static final long TWELVE_MINUTES_DURATION_IN_MS = 12 * 60 * 1000;
    private static final long THIRTEEN_MINUTES_DURATION_IN_MS = 13 * 60 * 1000;
    private static final long FOURTEEN_MINUTES_DURATION_IN_MS = 14 * 60 * 1000;
    private static final long EIGHTEEN_MINUTES_DURATION_IN_MS = 18 * 60 * 1000;
    private List<Trade> trades;
    private Map<String, List<Trade>> tradeMap = new HashMap<String, List<Trade>>();
    protected Map<String, StockTest> stockMap = new HashMap<String, StockTest>();


    public void setUpDataWithIn15Min() throws StockException {
        long time = new Date().getTime();
        Trade trade1 = new Trade(new Timestamp(time - FIVE_MINUTES_DURATION_IN_MS), new BigDecimal(80), TradeIndicator.SELL, new BigDecimal(150), "TEA");
        Trade trade2 = new Trade(new Timestamp(time - EIGHT_MINUTES_DURATION_IN_MS), new BigDecimal(90), TradeIndicator.BUY, new BigDecimal(120), "GIN");
        Trade trade3 = new Trade(new Timestamp(time - TWELVE_MINUTES_DURATION_IN_MS), new BigDecimal(100), TradeIndicator.SELL, new BigDecimal(130), "POP");
        Trade trade4 = new Trade(new Timestamp(time - THIRTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(110), TradeIndicator.BUY, new BigDecimal(140), "ALE");
        Trade trade5 = new Trade(new Timestamp(time - FOURTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(120), TradeIndicator.SELL, new BigDecimal(110), "JOE");
        Trade trade9 = new Trade(new Timestamp(time - FIVE_MINUTES_DURATION_IN_MS), new BigDecimal(80), TradeIndicator.SELL, new BigDecimal(150), "JOE");
        Trade trade8 = new Trade(new Timestamp(time - EIGHT_MINUTES_DURATION_IN_MS), new BigDecimal(90), TradeIndicator.BUY, new BigDecimal(120), "ALE");
        Trade trade7 = new Trade(new Timestamp(time - THIRTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(110), TradeIndicator.SELL, new BigDecimal(120), "POP");
        Trade trade6 = new Trade(new Timestamp(time - TWELVE_MINUTES_DURATION_IN_MS), new BigDecimal(100), TradeIndicator.BUY, new BigDecimal(130), "GIN");
        Trade trade10 = new Trade(new Timestamp(time - FOURTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(120), TradeIndicator.SELL, new BigDecimal(110), "TEA");

        trades = new ArrayList<Trade>();
        trades.add(trade1);
        trades.add(trade2);
        trades.add(trade3);
        trades.add(trade4);
        trades.add(trade5);
        trades.add(trade6);
        trades.add(trade7);
        trades.add(trade8);
        trades.add(trade9);
        trades.add(trade10);

        postTrades();
    }

    public void setUpDataBeyond15Min() throws StockException {
        Trade trade1 = new Trade(new Timestamp(new Date().getTime() - FIVE_MINUTES_DURATION_IN_MS), new BigDecimal(80), TradeIndicator.SELL, new BigDecimal(150), "TEA");
        Trade trade2 = new Trade(new Timestamp(new Date().getTime() - EIGHT_MINUTES_DURATION_IN_MS), new BigDecimal(90), TradeIndicator.BUY, new BigDecimal(120), "GIN");
        Trade trade3 = new Trade(new Timestamp(new Date().getTime() - TWELVE_MINUTES_DURATION_IN_MS), new BigDecimal(100), TradeIndicator.SELL, new BigDecimal(130), "POP");
        Trade trade4 = new Trade(new Timestamp(new Date().getTime() - THIRTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(110), TradeIndicator.BUY, new BigDecimal(140), "ALE");
        Trade trade5 = new Trade(new Timestamp(new Date().getTime() - FOURTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(120), TradeIndicator.SELL, new BigDecimal(110), "JOE");
        Trade trade9 = new Trade(new Timestamp(new Date().getTime() - EIGHTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(80), TradeIndicator.SELL, new BigDecimal(150), "JOE");
        Trade trade8 = new Trade(new Timestamp(new Date().getTime() - EIGHTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(90), TradeIndicator.BUY, new BigDecimal(120), "ALE");
        Trade trade7 = new Trade(new Timestamp(new Date().getTime() - EIGHTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(110), TradeIndicator.SELL, new BigDecimal(120), "POP");
        Trade trade6 = new Trade(new Timestamp(new Date().getTime() - EIGHTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(100), TradeIndicator.BUY, new BigDecimal(130), "GIN");
        Trade trade10 = new Trade(new Timestamp(new Date().getTime() - EIGHTEEN_MINUTES_DURATION_IN_MS), new BigDecimal(120), TradeIndicator.SELL, new BigDecimal(110), "TEA");

        trades = new ArrayList<Trade>();
        trades.add(trade1);
        trades.add(trade2);
        trades.add(trade3);
        trades.add(trade4);
        trades.add(trade5);
        trades.add(trade6);
        trades.add(trade7);
        trades.add(trade8);
        trades.add(trade9);
        trades.add(trade10);

        postTrades();
    }

    private void postTrades() {
        for (Trade trade : trades) {
            List<Trade> stockTrades = tradeMap.get(trade.getStockName());
            if (null == stockTrades) {
                stockTrades = new ArrayList<Trade>();
                tradeMap.put(trade.getStockName(), stockTrades);
            }
            TradesQueue.INSTANCE.recordTrade(trade);
            stockTrades.add(trade);
        }

        calculateGBCE(tradeMap);
    }

    private void calculateGBCE(Map<String, List<Trade>> tradesMap) {
        BigDecimal stockPrice = null;
        BigDecimal dividendYield = null;
        BigDecimal peRatio = null;
        LOGGER.info("Expected test results");
        LOGGER.info(String.format("%-30s", "-------------------------------------------------"));
        LOGGER.info(String.format("%-5s %-8s %-8s %-8s", "Symbol", "StockPrice", "DividendYield", "PE Ratio"));
        LOGGER.info(String.format("%-30s", "-------------------------------------------------"));
        for (StockStatic stockStatic : StockStatic.values()) {
            StockTest stock = new StockTest(stockStatic.name());
            List<Trade> stockTrades = tradesMap.get(stock.getStockSymbol());
            try {
                stockPrice = calculateStockPrice(stockTrades);
                dividendYield = calculateDividendYield(stockPrice, stockStatic);
                peRatio = calculatePERatio(stockPrice, dividendYield, stockStatic);

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            } finally {
                stock.setStockPrice(stockPrice);
                stock.setDividendYield(dividendYield);
                stock.setpERatio(peRatio);
                String format = ".2f";
                if (null == stock.getpERatio()) {
                    format = "s";
                }
                LOGGER.info(String.format("%-5s %-8.2f %-8.2f %-8" + format, stock.getStockSymbol(), stockPrice, stock.getDividendYield(), stock.getpERatio() == null ? "Na" : stock.getpERatio()));
            }
            stockMap.put(stock.getStockSymbol(), stock);

        }
    }

    private BigDecimal calculateStockPrice(List<Trade> trades) {
        BigDecimal totalTradePrice = BigDecimal.ZERO;
        BigDecimal totalSharesCount = BigDecimal.ZERO;
        BigDecimal stockPrice = null;
        for (Trade trade : trades) {
            totalTradePrice = totalTradePrice.add(trade.getTradePrice().multiply(trade.getTradeSharesCount()));
            totalSharesCount = totalSharesCount.add(trade.getTradeSharesCount());
        }
        if (null != totalTradePrice && null != totalSharesCount) {
            stockPrice = totalTradePrice.divide(totalSharesCount, 2, RoundingMode.CEILING);
        }

        return stockPrice;
    }

    private BigDecimal calculateDividendYield(BigDecimal stockPrice, StockStatic stockStatic) throws StockException {
        StockType stockType = stockStatic.getType();
        try {
            if (StockType.PREFERRED.equals(stockType)) {
                return (stockStatic.getFixedDividend().multiply(stockStatic.getParValue()).divide(stockPrice, 2, RoundingMode.CEILING));
            } else {
                BigDecimal lastDividend = stockStatic.getLastDividend();
                return lastDividend.divide(stockPrice, 2, RoundingMode.CEILING);
            }
        } catch (ArithmeticException ex) {
            throw new StockException("calculateDividendYield : Stock price  could have been zero for : " + stockStatic.name(), ex);
        }

    }

    private BigDecimal calculatePERatio(BigDecimal stockPrice, BigDecimal dividendYield, StockStatic stockStatic) throws StockException {
        try {
            return stockPrice.divide(dividendYield, 2, RoundingMode.CEILING);
        } catch (ArithmeticException ex) {
            throw new StockException("calculatePERatio : Dividend yield  could have been zero for : " + stockStatic.name(), ex);
        }
    }
}
