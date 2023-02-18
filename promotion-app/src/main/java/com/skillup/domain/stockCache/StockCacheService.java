package com.skillup.domain.stockCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCacheService {
    @Autowired
    StockCacheRepo stockCacheRepo;
    public Long getAvailableStock(String promotionId) {
        return stockCacheRepo.getPromotionAvailableStock(promotionId);
    }

    public boolean lockStock(StockOrderDomain stockOrderDomain) {
        return stockCacheRepo.lockStock(stockOrderDomain);
    }

    public boolean revertStock(StockOrderDomain stockOrderDomain) {
        return stockCacheRepo.revertStock(stockOrderDomain);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockCacheRepo.setPromotionAvailableStock(promotionId, availableStock);
    }
}
