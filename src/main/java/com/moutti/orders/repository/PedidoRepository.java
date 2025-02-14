package com.moutti.orders.repository;

import com.moutti.orders.model.Pedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Pedido> findById(Long aLong);

}
