package com.moutti.orders.controller;

import com.moutti.orders.enums.TopicKafkaEnum;
import com.moutti.orders.service.PedidoService;
import com.moutti.orders.kafka.ProducerPedido;
import com.moutti.orders.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;


    @Autowired
    ProducerPedido producerPedido;

    @GetMapping("/enviar")
    public String enviarPedido(@RequestParam Pedido pedido) {
        producerPedido.sendMessage(TopicKafkaEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
        return "Pedido enviado com sucesso!";
    }


}
