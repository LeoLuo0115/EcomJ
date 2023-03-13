package com.skillup.domain.promotionSql;

public interface StockOperation {
    boolean lockStock(String id);
    boolean deductStock(String id);
    boolean revertStock(String id);

}
