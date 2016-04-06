package com.jp.supersimplestocks.tradeandstockexchange;

import java.math.BigDecimal;

/**
 * Created by anusha
 */
public class StockTest {
    private String stockSymbol;
    private BigDecimal dividendYield;
    private BigDecimal pERatio;
    private BigDecimal stockPrice;


    public StockTest(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }

    public BigDecimal getpERatio() {
        return pERatio;
    }

    public void setpERatio(BigDecimal pERatio) {
        this.pERatio = pERatio;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

}
