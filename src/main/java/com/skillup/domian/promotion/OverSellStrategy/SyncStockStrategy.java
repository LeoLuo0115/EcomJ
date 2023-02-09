package com.skillup.domian.promotion.OverSellStrategy;

import com.skillup.domian.promotion.PromotionDomain;
import com.skillup.domian.promotion.PromotionRepository;
import com.skillup.domian.promotion.StockOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "sync")
public class SyncStockStrategy implements StockOperation {

    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public boolean lockStock(String id) {
        synchronized (this) {
            PromotionDomain promotionDomain = promotionRepository.getPromotionById(id);

            if (promotionDomain.getAvailableStock() > 0)  {
                promotionDomain.setAvailableStock(promotionDomain.getAvailableStock() - 1L);
                promotionDomain.setLockStock(promotionDomain.getLockStock() + 1);
                promotionRepository.updatePromotion(promotionDomain);
                System.out.println(promotionDomain.getPromotionName() + "start Synchronized locking... ");
                return true;
            }

            // System.out.println("Can not lock");
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
