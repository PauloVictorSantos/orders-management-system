package com.moutti.orders.enums;

public enum TopicKafkaEnum {
    PEDIDO_TOPIC("pedido-topic"),
    PEDIDO_PROCESSADO_TOPIC("pedido-processado-topic"),
    PEDIDO_CANCELADO_TOPIC("pedido-cancelado-topic");

    private final String topic;

    TopicKafkaEnum(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
