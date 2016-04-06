package com.jp.supersimplestocks.tradeandstockexchange;

import java.math.BigDecimal;
import java.math.RoundingMode;


// Ths class  holds the stock price , dividend yield and pe ratio for a stock symbol
class Stock {

    private final String stockSymbol;
    private BigDecimal dividendYield;
    private BigDecimal pERatio;
    private BigDecimal stockPrice;


    Stock(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    void setDividendYield(BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }

    public BigDecimal getpERatio() {
        return pERatio;
    }

    void setpERatio(BigDecimal pERatio) {
        this.pERatio = pERatio;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    void setStockPrice(BigDecimal stockPrice) throws StockException {
        this.stockPrice = stockPrice;
        StockStatic stockStatic = StockStatic.stockStaticNameMap.get(stockSymbol);
        calculateDividendYield(stockPrice, stockStatic);
        calculatePERatio(stockPrice);
    }

    private void calculateDividendYield(BigDecimal stockPrice, StockStatic stockStatic) throws StockException {
        StockType stockType = stockStatic.getType();
        try {
            if (StockType.PREFERRED.equals(stockType)) {
                this.dividendYield = (stockStatic.getFixedDividend().multiply(stockStatic.getParValue()).divide(stockPrice, 2, RoundingMode.CEILING));
            } else {
                BigDecimal lastDividend = stockStatic.getLastDividend();
                this.dividendYield = lastDividend.divide(stockPrice, 2, RoundingMode.CEILING);
            }
        } catch (ArithmeticException ex) {
            throw new StockException("calculateDividendYield : Stock price  could have been zero for : " + stockSymbol, ex);
        }

    }

    private void calculatePERatio(BigDecimal stockPrice) throws StockException {
        try {
            this.pERatio = stockPrice.divide(dividendYield, 2, RoundingMode.CEILING);
        } catch (ArithmeticException ex) {
            throw new StockException("calculatePERatio : Dividend yield  could have been zero for : " + stockSymbol, ex);
        }
    }

}
