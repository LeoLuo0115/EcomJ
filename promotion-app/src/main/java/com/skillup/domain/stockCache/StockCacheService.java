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

    public boolean lockStock(StockOrderDomain stockOrderDomain) {
        return stockCacheRepository.lockStock(stockOrderDomain);
    }

    public boolean revertStock(StockOrderDomain stockOrderDomain) {
        return stockCacheRepository.revertStock(stockOrderDomain);
    }

    public void setAvailableStock(String promotionId, Long availableStock) {
        stockCacheRepository.setPromotionAvailableStock(promotionId, availableStock);
    }
}
