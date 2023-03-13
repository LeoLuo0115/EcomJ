package com.skillup.domain.stockCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockCacheService {
    @Autowired
    StockCacheRepository stockCacheRepository;

    // getAvailableStock 最终是把 id 转换成 stockKey 拿到的 value，在 infra 层
    public Long getAvailableStock(String promotionId) {
        return stockCacheRepository.getPromotionAvailableStock(promotionId);
    }

    public boolean lockStock(String id) {
        return stockCacheRepository.lockStock(id);
    }

    public boolean revertStock(String id) {
        return stockCacheRepository.revertStock(id);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockCacheRepository.setPromotionAvailableStock(promotionId, availableStock);
    }
}
