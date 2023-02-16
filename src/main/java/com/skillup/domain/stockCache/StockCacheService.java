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

    public boolean lockStock(String id) {
        return stockCacheRepo.lockStock(id);
    }

    public boolean revertStock(String id) {
        return stockCacheRepo.revertStock(id);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockCacheRepo.setPromotionAvailableStock(promotionId, availableStock);
    }
}
