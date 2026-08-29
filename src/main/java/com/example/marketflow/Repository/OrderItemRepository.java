package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.marketflow.Order.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
