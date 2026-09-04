package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.payment.PaymentTransactionEntity;
import com.example.marketflow.payment.TransactionStatus;
import com.example.marketflow.payment.TransactionType;

@DataJpaTest
class PaymentTransactionRepositoryTest {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Test
    void shouldFindTransactionByIdempotencyKey() {
        PaymentTransactionEntity transaction = new PaymentTransactionEntity(
                15L,
                7L,
                TransactionType.PAYMENT,
                new BigDecimal("1000.00"),
                TransactionStatus.COMPLETED,
                "payment-order-15"
        );
        paymentTransactionRepository.saveAndFlush(transaction);

        assertTrue(
                paymentTransactionRepository.existsByIdempotencyKey("payment-order-15")
        );
        PaymentTransactionEntity found = paymentTransactionRepository
                .findByIdempotencyKey("payment-order-15")
                .orElseThrow();
        assertEquals(15L, found.getOrderId());
        assertEquals(TransactionStatus.COMPLETED, found.getStatus());
    }
}
