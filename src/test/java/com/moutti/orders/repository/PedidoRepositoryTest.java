package com.moutti.orders.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.moutti.orders.enums.StatusPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.moutti.orders.model.Pedido;
@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    public void shouldFindPedidosByStatus() {
        Pedido pedido1 = new Pedido();
        pedido1.setStatus(StatusPedido.RECEBIDO);

        Pedido pedido2 = new Pedido();
        pedido2.setStatus(StatusPedido.RECEBIDO);

        Pedido pedido3 = new Pedido();
        pedido3.setStatus(StatusPedido.PROCESSADO);

        pedidoRepository.saveAll(List.of(pedido1, pedido2, pedido3));

        List<Pedido> foundPedidos = pedidoRepository.findAll();

        assertThat(foundPedidos).hasSize(3);
        assertThat(foundPedidos).extracting(Pedido::getStatus)
                .contains(StatusPedido.RECEBIDO);
    }

    @Test
    public void shouldReturnEmptyListWhenStatusNotFound() {
        List<Pedido> foundPedidos = pedidoRepository.findAll();

        assertThat(foundPedidos).isEmpty();
    }
}