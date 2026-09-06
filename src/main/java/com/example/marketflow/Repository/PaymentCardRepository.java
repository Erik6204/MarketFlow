package com.example.marketflow.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.marketflow.payment_cards.PaymentCardEntity;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCardEntity, Long> {
    List<PaymentCardEntity> findAllByUserid(Long userid);

    List<PaymentCardEntity> findAllByUseridAndActiveTrue(Long userid);

    Optional<PaymentCardEntity> findByIdAndUseridAndActiveTrue(Long cardId, Long userid);

    @Modifying
    @Query("""
            UPDATE PaymentCardEntity card
            SET card.balance = card.balance - :amount
            WHERE card.id = :cardId
            AND card.userid = :buyerId
            AND card.active = true
            AND card.balance >= :amount
            """)
    int decreaseBalance(
            @Param("cardId") Long cardId,
            @Param("buyerId") Long buyerId,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
            UPDATE PaymentCardEntity card
            SET card.balance = card.balance + :amount
            WHERE card.id = :cardId
            AND card.userid = :buyerId
            """)
    int increaseBalance(
            @Param("cardId") Long cardId,
            @Param("buyerId") Long buyerId,
            @Param("amount") BigDecimal amount
    );
}
