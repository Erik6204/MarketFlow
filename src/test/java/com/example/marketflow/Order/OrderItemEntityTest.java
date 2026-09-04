package com.example.marketflow.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderItemEntityTest {

    @Test
    void constructorCalculatesTotalPrice() {
        OrderItemEntity item = new OrderItemEntity(
                1L,
                20L,
                5L,
                "Keyboard",
                new BigDecimal("49.90"),
                3,
                "keyboard.jpg"
        );

        assertEquals(0, new BigDecimal("149.70").compareTo(item.getTotalPrice()));
    }
}
