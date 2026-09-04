package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.Order.OrderEntity;
import com.example.marketflow.Order.OrderStatus;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldFindOrderOnlyForItsBuyer() {
        OrderEntity order = orderRepository.saveAndFlush(
                new OrderEntity(7L, OrderStatus.CREATED, new BigDecimal("1500.00"))
        );

        assertTrue(orderRepository.findByIdAndBuyerId(order.getId(), 7L).isPresent());
        assertTrue(orderRepository.findByIdAndBuyerId(order.getId(), 8L).isEmpty());
    }
}
