package com.jp.supersimplestocks.tradeandstockexchange;

import com.jp.supersimplestocks.tradeandstockexchange.StockException;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class TradesHolder {
   private BigDecimal totalTradePrice = BigDecimal.ZERO;
    private BigDecimal totalTradeSharesCount = BigDecimal.ZERO;

    void setTradeDetails(BigDecimal tradePrice, BigDecimal tradeSharesCount) {
        this.totalTradePrice = this.totalTradePrice.add(tradePrice.multiply(tradeSharesCount));
        this.totalTradeSharesCount = this.totalTradeSharesCount.add(tradeSharesCount);
    }

    BigDecimal getStockPrice() throws StockException {

        try {
            BigDecimal stockPrice = this.totalTradePrice.divide(this.totalTradeSharesCount, 2, RoundingMode.CEILING);
            return stockPrice;
        } catch (ArithmeticException ex) {
            throw new StockException("Total shares count could have been zero", ex);
        }
    }
}
