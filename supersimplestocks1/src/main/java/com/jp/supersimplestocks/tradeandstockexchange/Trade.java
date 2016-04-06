package com.jp.supersimplestocks.tradeandstockexchange;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Trade class is final and accessed in package only
 */
final class Trade {

    private Timestamp tradeRecordedTimeStamp;
    private BigDecimal tradeSharesCount;
    private TradeIndicator tradeBuyOrSellIndicator;
    private BigDecimal tradePrice;
    private String stockName;

    //Strict encapsulation
     Trade(Timestamp tradeRecordedTimeStamp, BigDecimal tradeSharesCount, TradeIndicator tradeBuyOrSellIndicator, BigDecimal tradePrice, String stockName) {
        this.tradeRecordedTimeStamp = tradeRecordedTimeStamp;
        this.tradeSharesCount = tradeSharesCount;
        this.tradeBuyOrSellIndicator = tradeBuyOrSellIndicator;
        this.tradePrice = tradePrice;
        this.stockName = stockName;
    }

    public Timestamp getTradeRecordedTimeStamp() {
        return tradeRecordedTimeStamp;
    }

    public BigDecimal getTradeSharesCount() {
        return tradeSharesCount;
    }

    public TradeIndicator getTradeBuyOrSellIndicator() {
        return tradeBuyOrSellIndicator;
    }

    public String getStockName() {
        return stockName;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public String toString() {
        return String.format("%-30s %-5s %-5s %-5s %-5s", tradeRecordedTimeStamp, stockName, tradeSharesCount, tradeBuyOrSellIndicator, tradePrice);
    }
}
