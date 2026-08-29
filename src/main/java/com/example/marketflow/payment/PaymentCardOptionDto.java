package com.example.marketflow.payment;

import java.math.BigDecimal;

public record PaymentCardOptionDto(//краткие и безопасные данные карты для ее отображения
        Long id,
        String maskedNumber,
        BigDecimal balance
) {
}