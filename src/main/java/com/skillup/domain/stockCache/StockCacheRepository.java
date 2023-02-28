package com.skillup.domain.stockCache;

public interface StockCacheRepository {
    boolean lockStock(String id);
    boolean revertStock(String id);

    Long getPromotionAvailableStock(String promotionId);

    void setPromotionAvailableStock(String promotionId, Long availableStock);
}
