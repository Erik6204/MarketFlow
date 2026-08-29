package com.example.marketflow.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.marketflow.Order.OrderEntity;

import jakarta.persistence.LockModeType;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndBuyerId(
            Long orderId,
            Long buyerId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT order
            FROM OrderEntity order
            WHERE order.id = :orderId
            AND order.buyerId = :buyerId
            """)
    Optional<OrderEntity> findForPayment(
            @Param("orderId") Long orderId,
            @Param("buyerId") Long buyerId
    );
}
