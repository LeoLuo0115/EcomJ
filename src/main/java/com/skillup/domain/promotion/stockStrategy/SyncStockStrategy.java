package com.skillup.domain.promotion.stockStrategy;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.StockOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="sync")
public class SyncStockStrategy implements StockOperation {


    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public boolean lockStock(String id) {
        System.out.println("inside sync op");
        synchronized (this){
            PromotionDomain promotionById = promotionRepository.getPromotionById(id);
            if (promotionById.getAvailableStock() >0) {
                promotionById.setAvailableStock(promotionById.getAvailableStock() - 1L);
                promotionById.setLockStock(promotionById.getLockStock()+1);
                promotionRepository.updatePromotion(promotionById);
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
