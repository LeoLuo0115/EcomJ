package com.skillup.application.order.mq.subscriber;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.mq.MqRepo;
import com.skillup.application.promotion.PromotionCacheApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${order.topic.pay-check}", consumerGroup =  "${order.topic.pay-check-group}")
public class PayCheckSubscriber implements RocketMQListener<MessageExt> {

    @Autowired
    OrderService orderService;

    @Autowired
    PromotionCacheApplication promotionCacheApplication;

    @Autowired
    MqRepo mqRepo;

    @Value("${promotion.topic.revert-stock}")
    String revertStockTopic;

    /**
     *
     * @param messageExt
     */
    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        // parse to String, if needed, parse to Object by FastJson2
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);
        log.info("OrderApp: received pay-check message.");

        // 1. get newest order info
        Long orderId = orderDomain.getOrderNumber();
        OrderDomain currentOrder = orderService.getOrderById(orderId);
        if (Objects.isNull(currentOrder)) {
            throw new RuntimeException("Order doesn't exist");
        }

        // 2. order status equals created = not pay
        if (currentOrder.getOrderStatus().equals(OrderStatus.CREATED)) {
            // 1. update order status to overtime
            currentOrder.setOrderStatus(OrderStatus.OVERTIME);
            orderService.updateOrder(currentOrder);
            log.info("update order status to OVERTIME");

            // 2. revert cache available_stock
            // TODO:　Idempotent
            ResponseEntity<Boolean> booleanResponseEntity = promotionCacheApplication.revertStock(orderDomain.getPromotionId());
            if (booleanResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST) || !booleanResponseEntity.hasBody() || Objects.isNull(booleanResponseEntity.getBody())) {
                log.error("Promotion is not exist");
            }
            boolean isReverted = Boolean.TRUE.equals(booleanResponseEntity.getBody());
            if (!isReverted) {
                throw new RuntimeException("Revert cache available stock failed!");
            }
            log.info("Redis: revert-stock successfully");

            // 3. database revert stock(available_stock, lock_sto ck)
            try {
                mqRepo.sendMessageToTopic(revertStockTopic, JSON.toJSONString(currentOrder));
            } catch (Exception e) {
                log.error("MySQL: revert-stock to DB failed");
            }

            log.info("OrderApp: sent revert-stock message to SQL");
        } else if (currentOrder.getOrderStatus().equals(OrderStatus.PAYED)){
            log.info("OrderId: " + orderDomain.getOrderNumber() + " is already payed");
        }  else if (currentOrder.getOrderStatus().equals(OrderStatus.OVERTIME)){
            log.info("OrderId: " + orderDomain.getOrderNumber() + " is already overtime");
        }
    }
}