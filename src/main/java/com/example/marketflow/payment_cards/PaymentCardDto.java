package com.example.marketflow.payment_cards;

import java.math.BigDecimal;

public record PaymentCardDto(
        Long id,
        String maskedNumber,
        BigDecimal balance,
        boolean active
) {
    public static PaymentCardDto from(PaymentCardEntity card) {
        return new PaymentCardDto(
                card.getId(),
                card.getMaskedNumber(),
                card.getBalance(),
                card.isActive()
        );
    }
}
