package com.moutti.orders.kafka;

import com.moutti.orders.model.Pedido;
import com.moutti.orders.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class ConsumerPedido {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerPedido.class);
    private PedidoService pedidoService;

    @KafkaListener(topics = "pedido-topic", groupId = "pedido-group", concurrency = "5")
    // 5 threads paralela para consumir mensagens do tópico pedido-topic
    public void consumerPedido(Pedido pedido) {
        try{
            pedidoService.salvarPedido(pedido);
            logger.info("Pedido consumido pelo listener e salvo: {}", pedido);
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem no listener Kafka: {}", pedido, e);
        }

        System.out.println("Pedido recebido: " + pedido);
    }
}
