package com.jp.supersimplestocks.tradeandstockexchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



/**
 * This enum holds the static information of each stock in GBCE
 */
public enum StockStatic {

    TEA(StockType.COMMON, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(100)),
    POP(StockType.COMMON, BigDecimal.valueOf(8), BigDecimal.ZERO, BigDecimal.valueOf(100)),
    ALE(StockType.COMMON, BigDecimal.valueOf(23), BigDecimal.ZERO, BigDecimal.valueOf(60)),
    GIN(StockType.PREFERRED, BigDecimal.valueOf(8), BigDecimal.valueOf(0.02), BigDecimal.valueOf(100)),
    JOE(StockType.COMMON, BigDecimal.valueOf(13), BigDecimal.ZERO, BigDecimal.valueOf(250));

    private StockType type;
    private BigDecimal lastDividend;
    private BigDecimal fixedDividend;
    private BigDecimal parValue;
    static Map<String, StockStatic> stockStaticNameMap;

    StockStatic(StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
    }

    static {
        stockStaticNameMap = new HashMap<String, StockStatic>();
        for (StockStatic stockStatic : StockStatic.values()) {
            stockStaticNameMap.put(stockStatic.name(), stockStatic);
        }

    }

    public StockType getType() {
        return type;
    }


    public BigDecimal getLastDividend() {
        return lastDividend;
    }


    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }


    public BigDecimal getParValue() {
        return parValue;
    }
}
