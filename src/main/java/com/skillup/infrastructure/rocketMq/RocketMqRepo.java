package com.skillup.infrastructure.rocketMq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;

@Repository
@Slf4j
public class RocketMqRepo {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public void sendMessageToTopic(String topic, String originMessage) {
        // 1. create rocketmq message
        Message message = new Message(topic, originMessage.getBytes(StandardCharsets.UTF_8));
        // 2. send rocketmq message to related topic
        try {
            rocketMQTemplate.getProducer().send(message);
            log.info("-- send message to rocketMQ --");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
