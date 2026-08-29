package com.example.marketflow.payment;

import java.math.BigDecimal;
import java.util.List;

public record PaymentPageDto(//показ данных всех карт и заказа
        Long orderId,
        BigDecimal totalPrice,
        PaymentStatus paymentStatus,
        List<PaymentCardOptionDto> cards,
        String idempotencyKey
) {
}
