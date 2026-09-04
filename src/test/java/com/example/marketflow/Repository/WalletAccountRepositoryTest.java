package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.payment.WalletAccountEntity;

import jakarta.persistence.EntityManager;

@DataJpaTest
class WalletAccountRepositoryTest {

    @Autowired
    private WalletAccountRepository walletAccountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldIncreaseWalletBalanceAtomically() {
        WalletAccountEntity wallet = walletAccountRepository.saveAndFlush(
                new WalletAccountEntity(7L)
        );

        int updatedRows = walletAccountRepository.increaseBalance(
                7L,
                new BigDecimal("90.00")
        );
        entityManager.flush();
        entityManager.clear();

        WalletAccountEntity updated = walletAccountRepository
                .findById(wallet.getId())
                .orElseThrow();
        assertEquals(1, updatedRows);
        assertEquals(0, new BigDecimal("90.00").compareTo(updated.getBalance()));
    }
}
