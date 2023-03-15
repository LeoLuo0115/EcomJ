package com.skillup.domain.stockCache;

public interface StockCacheRepository {
    boolean lockStock(StockOrderDomain stockOrderDomain);
    boolean revertStock(StockOrderDomain stockOrderDomain);

    Long getPromotionAvailableStock(String promotionId);

    void setPromotionAvailableStock(String promotionId, Long availableStock);
}
