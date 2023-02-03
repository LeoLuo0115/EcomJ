package com.skillup.domain.promotion;

public interface StockOperation {
        boolean lockStock(String id);
        boolean deductStock(String id);
        boolean revertStock(String id);
    }

