package com.skillup.domain.promotion.stockStrategy;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.StockOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service(value = "sync")
@Slf4j
public class SyncStockStrategy implements StockOperation {

    @Autowired
    PromotionRepository promotionRepository;
    @Override
    public boolean lockStock(String id) {
        synchronized (this) {
            PromotionDomain promotionDomain = promotionRepository.getPromotionById(id);
            if (promotionDomain.getAvailableStock() > 0) {
                promotionDomain.setAvailableStock(promotionDomain.getAvailableStock() - 1L);
                promotionDomain.setLockStock(promotionDomain.getLockStock() + 1L);
                promotionRepository.updatePromotion(promotionDomain);
                log.info(promotionDomain.getPromotionName() + "Start Sync locking...");
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean deductStock(String id) {
        return false;
    }

    @Override
    public boolean revertStock(String id) {
        return false;
    }
}
