package com.example.marketflow.payment;

import com.example.marketflow.payment_cards.PaymentCardEntity;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentCardOptionDto convert(PaymentCardEntity entity) {
        return new PaymentCardOptionDto(
                entity.getId(),
                entity.getMaskedNumber(),
                entity.getBalance()
        );
    }
}
