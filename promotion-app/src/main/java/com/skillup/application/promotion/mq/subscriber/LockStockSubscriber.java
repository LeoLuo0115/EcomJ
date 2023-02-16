package com.skillup.application.promotion.mq.subscriber;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionStockLog.PromotionStockLogDomain;
import com.skillup.domain.promotionStockLog.PromotionStockLogService;
import com.skillup.domain.promotionStockLog.util.OperationName;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${promotion.topic.lock-stock}", consumerGroup = "${promotion.topic.lock-stock-group}")
public class LockStockSubscriber implements RocketMQListener<MessageExt> {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionStockLogService promotionStockLogService;

    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);
        log.info("PromotionApp: received lock-stock message. OrderId: " + orderDomain.getOrderNumber());

        // idempotent
        PromotionStockLogDomain promotionStockLogDomain = promotionStockLogService.getPromotionDomain(orderDomain.getOrderNumber(), OperationName.LOCK_STOCK.name());
        if (Objects.nonNull(promotionStockLogDomain)) return;

        promotionService.lockStock(orderDomain.getPromotionId());
        promotionStockLogService.createPromotionLog(createLockStockDomain(orderDomain));
    }

    private PromotionStockLogDomain createLockStockDomain(OrderDomain orderDomain) {
        return PromotionStockLogDomain.builder()
                .createTime(LocalDateTime.now())
                .promotionId(orderDomain.getPromotionId())
                .orderNumber(orderDomain.getOrderNumber())
                .operationName(OperationName.LOCK_STOCK)
                .userId(orderDomain.getUserId())
                .build();
    }
}
