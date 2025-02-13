package com.moutti.orders.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.moutti.orders.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal vTotalPed;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude // Evita loop infinito no toString()
    @JsonManagedReference // Marca o lado "pai"
    private List<Item> itens = new ArrayList<>();



}
