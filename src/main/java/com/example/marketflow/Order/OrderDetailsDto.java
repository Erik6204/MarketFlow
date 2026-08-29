package com.example.marketflow.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.marketflow.payment.PaymentStatus;

public record OrderDetailsDto(
        Long id,
        OrderStatus status,
        PaymentStatus paymentStatus,
        BigDecimal totalPrice,
        Instant createdAt,
        List<OrderItemDto> items
) {
}
