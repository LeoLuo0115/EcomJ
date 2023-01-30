package com.skillup.domain.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PromotionDomainService {

    @Autowired
    PromotionRepository promotionRepository;

    @Resource(name="${promotion.stock-strategy}")
    StockOperation stockOperation;

    public PromotionDomain createPromotion(PromotionDomain promotionDomain){
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id){
        return promotionRepository.getPromotionById(id);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByStatus(status);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockstock(String id) {
        return stockOperation.lockStock(id);


    }

    public boolean deductStock(String id) {
        return stockOperation.deductStock(id);
    }

    public boolean revertStock(String id) {
        return stockOperation.revertStock(id);
    }
}
