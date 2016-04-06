package com.jp.supersimplestocks.tradeandstockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum TradesQueue {
    INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(TradesQueue.class);
    public static final int STOCK_DISPLAY_INTERVAL = 15 * 60 * 1000;
    private final ConcurrentLinkedQueue<Trade> trades;
    private volatile Timestamp evictionThresholdTimestamp;


    private TradesQueue() {
        trades = new ConcurrentLinkedQueue<Trade>();
    }

    /**
     * This method provides thread safe adding trades to the data structure
     *
     * @param trade
     */
    void recordTrade(Trade trade) {
        trades.offer(trade);
        LOGGER.info(trade.toString());
    }

    /**
     * This method returns the stock price calculated from trades in the last 15 minutes
     *
     * @return
     */
    Map<String, TradesHolder> getStockPriceFromTrades(Timestamp currentTimestamp) {
        Timestamp startTimestamp;
        Timestamp tradeRecordedTimeStamp;
        long startTimestampMilliSeconds = currentTimestamp.getTime() - STOCK_DISPLAY_INTERVAL;
        startTimestamp = new Timestamp(startTimestampMilliSeconds);
        String calculationDuration = "Calculate stock price from : " + startTimestamp + " to " + currentTimestamp;
        Map<String, TradesHolder> stocksMap = new HashMap<String, TradesHolder>();
        TradesHolder tradesHolder;
        boolean startAudit = true;
        for (Trade trade : trades) {
            tradeRecordedTimeStamp = trade.getTradeRecordedTimeStamp();
            if ((tradeRecordedTimeStamp.equals(startTimestamp) || tradeRecordedTimeStamp.after(startTimestamp))) {
                if ((tradeRecordedTimeStamp.equals(currentTimestamp) || tradeRecordedTimeStamp.before(currentTimestamp))) {
                    tradesHolder = stocksMap.get(trade.getStockName());
                    if (null == tradesHolder) {
                        tradesHolder = new TradesHolder();
                        stocksMap.put(trade.getStockName(), tradesHolder);
                    }
                    tradesHolder.setTradeDetails(trade.getTradePrice(), trade.getTradeSharesCount());
                    if (startAudit) {
                        LOGGER.info(calculationDuration);
                        LOGGER.info("Considering the below trades...");
                        startAudit = false;
                    }
                    LOGGER.info(trade.toString());
                    evictionThresholdTimestamp = startTimestamp;
                }
            }
        }
        LOGGER.info("evictionThresholdTimestamp set to "+evictionThresholdTimestamp);
        return stocksMap;
    }

    void evictTrades() {
        for (Trade trade : trades) {
            Timestamp tradeRecordedTimeStamp = trade.getTradeRecordedTimeStamp();
            if (null != evictionThresholdTimestamp) {
                if (evictionThresholdTimestamp.after(tradeRecordedTimeStamp)) {
                    LOGGER.info("Evicting trade :" + trade);
                    trades.remove(trade);
                }
            }
        }
    }

    void clear() {
        trades.clear();
    }
}
