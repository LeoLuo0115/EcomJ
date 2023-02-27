package com.skillup.application.order.mq;

public interface MqRepo {
    public void sendMessageToTopic(String topic, String originMessage);

    public void sendDelayMessageToTopic(String topic, String originMessage);
}
