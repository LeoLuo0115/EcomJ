package com.skillup.domain.promotion.stockStrategy;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.StockOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "oversell")
public class OverSellStrategy implements StockOperation {

    @Autowired
    PromotionRepository promotionRepository;
    @Override
    public boolean lockStock(String id) {
        PromotionDomain promotionDomain = promotionRepository.getPromotionById(id);
        if (promotionDomain.getAvailableStock() > 0) {
            promotionDomain.setAvailableStock(promotionDomain.getAvailableStock() - 1L);
            promotionDomain.setLockStock(promotionDomain.getLockStock() + 1L);
            promotionRepository.updatePromotion(promotionDomain);
            System.out.println(promotionDomain.getPromotionName() + "Start OverSell locking...");
            return true;
        }
        return false;
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
