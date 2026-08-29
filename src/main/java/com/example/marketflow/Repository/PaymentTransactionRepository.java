package com.example.marketflow.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.marketflow.payment.PaymentTransactionEntity;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long> {

    boolean existsByIdempotencyKey(String idempotencyKey);

    List<PaymentTransactionEntity> findAllByOrderId(Long orderId);

    Optional<PaymentTransactionEntity> findByIdempotencyKey(String idempotencyKey);
}
