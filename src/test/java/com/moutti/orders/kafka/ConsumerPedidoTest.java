package com.moutti.orders.kafka;


import com.moutti.orders.model.Pedido;
import com.moutti.orders.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class ConsumerPedidoTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private ConsumerPedido consumerPedido;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumerPedido() {
        Pedido pedido = new Pedido();
        // Configure o objeto Pedido conforme necessário

        consumerPedido.consumerPedido(pedido);

        verify(pedidoService, times(1)).salvarPedido(pedido);
    }

    @Test
    public void testConsumerPedido_Exception() {
        Pedido pedido = new Pedido();
        // Configure o objeto Pedido conforme necessário

        doThrow(new RuntimeException("Erro ao salvar pedido")).when(pedidoService).salvarPedido(pedido);

        consumerPedido.consumerPedido(pedido);

        verify(pedidoService, times(1)).salvarPedido(pedido);
    }
}