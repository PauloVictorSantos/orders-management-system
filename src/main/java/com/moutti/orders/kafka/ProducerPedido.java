package com.moutti.orders.kafka;
import com.moutti.orders.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerPedido {

    private Logger log = LoggerFactory.getLogger(ProducerPedido.class);
    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;


    public void sendMessage(String topico, Pedido pedido) {
        log.info("Enviando pedido para o tópico: {} - Pedido: {}", topico, pedido);
        kafkaTemplate.send(topico, pedido);
        log.info("Pedido enviado com sucesso para o tópico: {}", topico);
    }
}