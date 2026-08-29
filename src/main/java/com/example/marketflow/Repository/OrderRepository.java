package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.marketflow.Order.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
