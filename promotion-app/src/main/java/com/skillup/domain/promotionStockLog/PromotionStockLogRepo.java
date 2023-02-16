package com.skillup.domain.promotionStockLog;

public interface PromotionStockLogRepo {
    public void createPromotionLog(PromotionStockLogDomain promotionLogDomain);

    public PromotionStockLogDomain getByOrderIdAndOperation(Long orderId, String operationName);
}
