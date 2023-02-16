package com.skillup.application.order.mq.subscriber;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.mq.MqRepo;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${order.topic.create-order}", consumerGroup = "${order.topic.create-order-group}")
public class CreateOrderSubscriber implements RocketMQListener<MessageExt> {

    @Autowired
    OrderService orderService;

    @Autowired
    MqRepo mqRepo;

    @Value("${promotion.topic.lock-stock}")
    String lockStockTopic;

    @Value("${order.topic.pay-check}")
    String payCheckTopic;

    @Override
    public void onMessage(MessageExt messageExt) {
        // parse to String, if needed, parse to Object by FastJson2
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);
        log.info("OrderApp: received create order message. OrderId is: " + orderDomain.getOrderNumber());

        // 0. Idempotent
        OrderDomain exitsOrder = orderService.getOrderById(orderDomain.getOrderNumber());
        if (!Objects.isNull(exitsOrder)) return;

        // 1. fulfillment order detail, crate order
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        orderDomain.setCreateTime(LocalDateTime.now());
        OrderDomain savedOrderDomain = orderService.createOrder(orderDomain);

        // 2. send "lock-stock" message
        mqRepo.sendMessageToTopic(lockStockTopic, JSON.toJSONString(savedOrderDomain));
        log.info("OrderApp: sent lock-stock message. OrderId is: " + orderDomain.getOrderNumber());

        // 3. send "pay-check" message
        mqRepo.sendDelayMessageToTopic(payCheckTopic, JSON.toJSONString(savedOrderDomain));
        log.info("OrderApp: sent pay-check message, OrderId is: " + orderDomain.getOrderNumber());
    }
}