package com.moutti.orders.kafka;

import com.moutti.orders.model.Pedido;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class ConsumerPedido {


    @KafkaListener(topics = "pedido-topic", groupId = "pedido-group,", concurrency = "5")
    public void consumerPedido(Pedido pedido) {
        //validarduplicidade
        //salvar
        System.out.println("Pedido recebido: " + pedido);
    }
}
