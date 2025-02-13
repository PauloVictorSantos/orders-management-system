package com.moutti.orders.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Entity
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @ToString.Exclude // Evita loop infinito no toString()
    @JsonBackReference // Marca o lado "filho"
    private Pedido pedido;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private int qtd;

    private BigDecimal vTotalItem;
}
