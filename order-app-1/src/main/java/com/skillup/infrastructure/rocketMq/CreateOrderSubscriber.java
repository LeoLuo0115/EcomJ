package com.skillup.infrastructure.rocketMq;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.event.CreateOrderEvent;
import com.skillup.domain.order.OrderDomain;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${order.topic.create-order}", consumerGroup = "${order.topic.create-order-group}")
public class CreateOrderSubscriber implements RocketMQListener<MessageExt> {

    @Autowired
    ApplicationContext context;

    @Override
    public void onMessage(MessageExt messageExt) {
        // parse to String, if needed, parse to Object by FastJson2
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        OrderDomain orderDomain = JSON.parseObject(messageBody, OrderDomain.class);
        log.info("OrderApp: received create order message. OrderId is: " + orderDomain.getOrderNumber());

        CreateOrderEvent event = new CreateOrderEvent(this, orderDomain);
        context.publishEvent(event);
    }
}