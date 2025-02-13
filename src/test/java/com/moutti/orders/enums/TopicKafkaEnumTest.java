package com.moutti.orders.enums;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopicKafkaEnumTest {

    @Test
    public void testPedidoTopicValue() {
        assertEquals("pedido-topic", TopicKafkaEnum.PEDIDO_TOPIC.getTopic());
    }

    @Test
    public void testPedidoProcessadoTopicValue() {
        assertEquals("pedido-processado-topic", TopicKafkaEnum.PEDIDO_PROCESSADO_TOPIC.getTopic());
    }

    @Test
    public void testPedidoCanceladoTopicValue() {
        assertEquals("pedido-cancelado-topic", TopicKafkaEnum.PEDIDO_CANCELADO_TOPIC.getTopic());
    }

    @Test
    public void testEnumValues() {
        TopicKafkaEnum[] values = TopicKafkaEnum.values();
        assertEquals(3, values.length);
        assertTrue(containsEnumValue(values, TopicKafkaEnum.PEDIDO_TOPIC));
        assertTrue(containsEnumValue(values, TopicKafkaEnum.PEDIDO_PROCESSADO_TOPIC));
        assertTrue(containsEnumValue(values, TopicKafkaEnum.PEDIDO_CANCELADO_TOPIC));
    }

    private boolean containsEnumValue(TopicKafkaEnum[] values, TopicKafkaEnum value) {
        for (TopicKafkaEnum enumValue : values) {
            if (enumValue == value) {
                return true;
            }
        }
        return false;
    }
}