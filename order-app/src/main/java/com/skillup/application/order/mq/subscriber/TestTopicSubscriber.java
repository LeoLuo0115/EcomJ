package com.skillup.application.order.mq.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-group")
public class TestTopicSubscriber implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        // parse to String, if needed, parse to Object by FastJson2
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        // System.out.println("messageId: " + messageExt.getMsgId());
        System.out.println(messageBody);
    }
}