package com.jp.supersimplestocks.tradeandstockexchange;


public enum TradeIndicator {

    BUY(0),
    SELL(1);

    private int tradeIndicator;

    private TradeIndicator(int tradeIndicator) {
        this.tradeIndicator = tradeIndicator;
    }

    public int getTradeIndicator() {
        return tradeIndicator;
    }
}
