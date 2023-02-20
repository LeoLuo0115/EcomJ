package com.skillup.domain.stockCache;

public interface StockCacheRepository {
    Long getAvailableStockById(String promotionId);

    boolean lockStock(String id);

    boolean revertStock(String id);

    void setPromotionAvailableStock(String promotionId, Long availableStock);
}
