package com.skillup.domain.promotionStockLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionStockLogService {

    @Autowired
    PromotionStockLogRepo promotionStockLogRepo;

    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionStockLogDomain createPromotionLog(PromotionStockLogDomain domain) {
        promotionStockLogRepo.createPromotionLog(domain);
        return domain;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionStockLogDomain getPromotionDomain(Long orderId, String operationName) {
        return promotionStockLogRepo.getByOrderIdAndOperation(orderId, operationName);
    }
}
