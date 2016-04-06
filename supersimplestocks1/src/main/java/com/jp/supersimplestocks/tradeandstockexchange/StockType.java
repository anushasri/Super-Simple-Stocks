package com.jp.supersimplestocks.tradeandstockexchange;


public enum StockType {
    PREFERRED("Preferred"),
    COMMON("Common");

    private String type;

    StockType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
