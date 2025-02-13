
package com.moutti.orders.controller;

import com.moutti.orders.enums.TopicKafkaEnum;
import com.moutti.orders.kafka.ProducerPedido;
import com.moutti.orders.model.Pedido;
import com.moutti.orders.service.PedidoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @Mock
    private ProducerPedido producerPedido;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testListarPedidos() {
        List<Pedido> pedidos = Collections.singletonList(new Pedido());
        Mockito.when(pedidoService.listarPedidos()).thenReturn(pedidos);

        ResponseEntity<List<Pedido>> response = pedidoController.listarPedidos();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(pedidos, response.getBody());
        Mockito.verify(pedidoService, Mockito.times(1)).listarPedidos();
    }

    @Test
    public void testCriarPedido() {
        Pedido pedido = new Pedido();

        ResponseEntity<Pedido> response = pedidoController.criarPedido(pedido);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pedido, response.getBody());
        verify(pedidoService, times(1)).salvarPedido(pedido);
        verify(producerPedido, times(1)).sendMessage(TopicKafkaEnum.PEDIDO_TOPIC.getTopic(), pedido);
    }


    @Test
    public void testCancelarPedido() {
        Long id = 1L;
        Pedido pedidoCancelado = new Pedido();
        when(pedidoService.cancelarPedido(id)).thenReturn(pedidoCancelado);

        ResponseEntity<Pedido> response = pedidoController.cancelarPedido(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoCancelado, response.getBody());
        verify(pedidoService, times(1)).cancelarPedido(id);
    }
}