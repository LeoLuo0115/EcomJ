package com.skillup.infrastructure.repoImplement;

import com.skillup.domain.promotionStockLog.PromotionStockLogDomain;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DomainRecord<D, R> {
    @Transactional(propagation = Propagation.REQUIRED)
    void createPromotionLog(PromotionStockLogDomain promotionLogDomain);

    @Transactional(propagation = Propagation.REQUIRED)
    PromotionStockLogDomain getByOrderIdAndOperation(Long orderId, String operationName);

    public D toDomain(R r);
    public R toRecord(D d);
}
