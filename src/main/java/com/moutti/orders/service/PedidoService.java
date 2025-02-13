package com.moutti.orders.service;

import com.moutti.orders.enums.StatusPedido;
import com.moutti.orders.enums.TopicKafkaEnum;
import com.moutti.orders.kafka.ProducerPedido;
import com.moutti.orders.model.Pedido;
import com.moutti.orders.model.Produto;
import com.moutti.orders.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional // Garantia de consistência nos commits e rollbacks
public class PedidoService {
    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProducerPedido producerPedido;
    public void salvarPedido(Pedido pedido) {

        verificarDuplicidade(pedido);
        // Calcula o valor total
        calcularValorTotalPed(pedido);
        // Processa o pedido por ID e envia para outro tópico
        processarPedido(pedido);
        // Vetifica itens do pedido
        logger.info("Pedido com itens: {}", pedido.getItens());

    }

    private static void calcularValorTotalPed(Pedido pedido) {
        BigDecimal valorTotal = pedido.getItens().stream()
                .map(item -> item.getProduto().getPrec().multiply(BigDecimal.valueOf(item.getQtd())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setVTotalPed(valorTotal);
    }

    public void processarPedido(Pedido pedido) {
        pedido.setStatus(StatusPedido.PROCESSADO);
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido processado para o tópico Kafka");
        producerPedido.sendMessage(TopicKafkaEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
    }

    private void verificarDuplicidade(Pedido pedido) {
        List<Pedido> pedidosExistentes = pedidoRepository.findAll();
        List<String> itensAtuais = formatarListPedido(pedido);

        // Itera sobre os pedidos existentes para verificar duplicidade
        for (Pedido pedidoExistente : pedidosExistentes) {
            // Valida se o número de itens é igual
            if (pedidoExistente.getItens().size() != pedido.getItens().size()) {
                continue;
            }

            // Formata a lista de itens do pedido existente
            List<String> itensExistentes = formatarListPedido(pedidoExistente);

            // Verifica se todos os itens são iguais
            verificarPedidoDuplicados(itensAtuais, itensExistentes);
        }
    }

    private static void verificarPedidoDuplicados(List<String> itensAtuais, List<String> itensExistentes) {
        if (itensAtuais.containsAll(itensExistentes) && itensExistentes.containsAll(itensAtuais)) {
            logger.error("Pedido duplicado detectado com os seguintes itens: {}", itensAtuais);
            throw new RuntimeException("Pedido duplicado detectado. Estrutura: " + itensAtuais);
        }
    }

    private static List<String> formatarListPedido(Pedido pedido) {
        // Formata a lista de itens do pedido atual para o padrão "nome-preco-quantidade"
        List<String> itens = pedido.getItens().stream()
                .map(item -> {
                    Produto produto = item.getProduto();
                    return produto.getDescr() + "-" + produto.getPrec() + "-" + item.getQtd();
                })
                .toList();
        return itens;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // Cancela o pedido por ID e envia para outro tópico
    public Pedido cancelarPedido(Long id) {
        logger.info("Iniciando cancelamento do pedido ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com ID: {}", id);
                    return new RuntimeException("Pedido não encontrado com ID: " + id);
                });
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido cancelado para o tópico Kafka");
        producerPedido.sendMessage(TopicKafkaEnum.PEDIDO_CANCELADO_TOPIC.getTopic(), pedido);
        return pedido;
    }

}
