package com.skillup.application.order.event;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.mq.MqRepo;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class CreateOrderEventHandler implements ApplicationListener<CreateOrderEvent> {
    @Autowired
    OrderService orderService;

    @Autowired
    MqRepo mqRepo;

    @Value("${promotion.topic.lock-stock}")
    String lockStockTopic;

    @Value("${order.topic.pay-check}")
    String payCheckTopic;

    @Override
    @Transactional
    public void onApplicationEvent(CreateOrderEvent event) {
        OrderDomain orderDomain = event.orderDomain;

        // 0. Idempotent
        OrderDomain exitsOrder = orderService.getOrderById(orderDomain.getOrderNumber());
        if (!Objects.isNull(exitsOrder)) return;

        // 1. fulfillment order detail, crate order
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        orderDomain.setCreateTime(LocalDateTime.now());
        OrderDomain savedOrderDomain = orderService.createOrder(orderDomain);

        /**
         * step 2 and step 3 can send single message, and have 2 separate listeners
         */
        // 2. send "lock-stock" message
        mqRepo.sendMessageToTopic(lockStockTopic, JSON.toJSONString(savedOrderDomain));
        log.info("OrderApp: sent lock-stock message. OrderId is: " + orderDomain.getOrderNumber());

        // 3. send "pay-check" message
        mqRepo.sendDelayMessageToTopic(payCheckTopic, JSON.toJSONString(savedOrderDomain));
        log.info("OrderApp: sent pay-check message, OrderId is: " + orderDomain.getOrderNumber());
    }
}
