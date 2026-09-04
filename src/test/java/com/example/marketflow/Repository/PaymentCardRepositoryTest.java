package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.payment_cards.PaymentCardEntity;

import jakarta.persistence.EntityManager;

@DataJpaTest
class PaymentCardRepositoryTest {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldDecreaseBalanceOnlyForCardOwnerWhenFundsAreSufficient() {
        PaymentCardEntity card = paymentCardRepository.saveAndFlush(
                card(7L, "100.00")
        );

        int updatedRows = paymentCardRepository.decreaseBalance(
                card.getId(),
                7L,
                new BigDecimal("40.00")
        );
        entityManager.flush();
        entityManager.clear();

        PaymentCardEntity updated = paymentCardRepository.findById(card.getId()).orElseThrow();
        assertEquals(1, updatedRows);
        assertEquals(0, new BigDecimal("60.00").compareTo(updated.getBalance()));
    }

    @Test
    void shouldNotDecreaseBalanceWhenFundsAreInsufficient() {
        PaymentCardEntity card = paymentCardRepository.saveAndFlush(
                card(7L, "25.00")
        );

        int updatedRows = paymentCardRepository.decreaseBalance(
                card.getId(),
                7L,
                new BigDecimal("40.00")
        );
        entityManager.flush();
        entityManager.clear();

        PaymentCardEntity unchanged = paymentCardRepository.findById(card.getId()).orElseThrow();
        assertEquals(0, updatedRows);
        assertEquals(0, new BigDecimal("25.00").compareTo(unchanged.getBalance()));
    }

    @Test
    void shouldNotReturnActiveCardToAnotherUser() {
        PaymentCardEntity card = paymentCardRepository.saveAndFlush(
                card(7L, "100.00")
        );

        assertTrue(
                paymentCardRepository
                        .findByIdAndUseridAndActiveTrue(card.getId(), 8L)
                        .isEmpty()
        );
    }

    private PaymentCardEntity card(Long userId, String balance) {
        return new PaymentCardEntity(
                userId,
                "secret-token-" + userId,
                "**** **** **** 4242",
                new BigDecimal(balance)
        );
    }
}
