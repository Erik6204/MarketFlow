package com.example.marketflow.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.marketflow.payment.PaymentStatus;

class OrderMapperTest {

    @Test
    void toDetailsDtoMapsOrderAndItsItems() {
        OrderEntity order = new OrderEntity(
                7L,
                OrderStatus.CREATED,
                new BigDecimal("100.00")
        );
        OrderItemEntity item = new OrderItemEntity(
                1L,
                20L,
                5L,
                "Keyboard",
                new BigDecimal("50.00"),
                2,
                "keyboard.jpg"
        );

        OrderDetailsDto result = OrderMapper.toDetailsDto(order, List.of(item));

        assertEquals(OrderStatus.CREATED, result.status());
        assertEquals(PaymentStatus.NOT_PAID, result.paymentStatus());
        assertEquals(1, result.items().size());
        assertEquals(20L, result.items().getFirst().productId());
        assertEquals(0, new BigDecimal("100.00")
                .compareTo(result.items().getFirst().totalPrice()));
    }
}
