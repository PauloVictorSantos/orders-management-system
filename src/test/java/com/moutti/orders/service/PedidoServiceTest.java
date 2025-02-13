package com.moutti.orders.service;

import com.moutti.orders.enums.StatusPedido;
import com.moutti.orders.enums.TopicKafkaEnum;
import com.moutti.orders.kafka.ProducerPedido;
import com.moutti.orders.model.Pedido;
import com.moutti.orders.model.Produto;
import com.moutti.orders.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProducerPedido producerPedido;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSalvarPedido() {
        Pedido pedido = new Pedido();
        pedido.setItens(Collections.emptyList());

        doNothing().when(pedidoService).verificarDuplicidade(pedido);
        doNothing().when(pedidoService).calcularValorTotalPed(pedido);
        doNothing().when(pedidoService).processarPedido(pedido);

        pedidoService.salvarPedido(pedido);

        verify(pedidoService, times(1)).verificarDuplicidade(pedido);
        verify(pedidoService, times(1)).calcularValorTotalPed(pedido);
        verify(pedidoService, times(1)).processarPedido(pedido);
    }

    @Test
    public void testProcessarPedido() {
        Pedido pedido = new Pedido();
        pedido.setItens(Collections.emptyList());

        pedidoService.processarPedido(pedido);

        assertEquals(StatusPedido.PROCESSADO, pedido.getStatus());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(producerPedido, times(1)).sendMessage(TopicKafkaEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
    }

    @Test
    public void testCancelarPedido() {
        Long id = 1L;
        Pedido pedido = new Pedido();
        pedido.setItens(Collections.emptyList());

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.cancelarPedido(id);

        assertEquals(StatusPedido.CANCELADO, result.getStatus());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(producerPedido, times(1)).sendMessage(TopicKafkaEnum.PEDIDO_CANCELADO_TOPIC.getTopic(), pedido);
    }

    @Test
    public void testCancelarPedido_NotFound() {
        Long id = 1L;

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.cancelarPedido(id);
        });

        assertEquals("Pedido não encontrado com ID: " + id, exception.getMessage());
        verify(pedidoRepository, times(1)).findById(id);
    }
}