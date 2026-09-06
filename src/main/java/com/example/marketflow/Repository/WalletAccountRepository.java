package com.example.marketflow.Repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.marketflow.payment.WalletAccountEntity;

@Repository
public interface WalletAccountRepository extends JpaRepository<WalletAccountEntity, Long> {
    Optional<WalletAccountEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Modifying
    @Query(value = """
            UPDATE wallet_accounts
            SET balance = balance + :amount,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = :userId
            """, nativeQuery = true)
    int increaseBalance(
            @Param("userId") Long userId,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query(value = """
            UPDATE wallet_accounts
            SET balance = balance - :amount,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = :userId
            AND balance >= :amount
            """, nativeQuery = true)
    int decreaseBalance(
            @Param("userId") Long userId,
            @Param("amount") BigDecimal amount
    );

    @Query(value = """
            SELECT wa.*
            FROM wallet_accounts wa
            JOIN user_roles ur ON ur.user_id = wa.user_id
            JOIN roles r ON r.id = ur.role_id
            WHERE r.name = 'OWNER'
            """, nativeQuery = true)
    Optional<WalletAccountEntity> findOwnerAccount();
}
