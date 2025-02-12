package com.moutti.orders.model;

import com.moutti.orders.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
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
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<Item> itens;
}
