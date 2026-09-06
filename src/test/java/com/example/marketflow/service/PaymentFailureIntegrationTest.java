package com.example.marketflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.marketflow.Order.OrderEntity;
import com.example.marketflow.Order.OrderStatus;
import com.example.marketflow.Repository.OrderRepository;
import com.example.marketflow.Repository.PaymentCardRepository;
import com.example.marketflow.Repository.PaymentTransactionRepository;
import com.example.marketflow.exception.InsufficientFundsException;
import com.example.marketflow.payment.PayOrderRequest;
import com.example.marketflow.payment.PaymentStatus;
import com.example.marketflow.payment.TransactionStatus;
import com.example.marketflow.payment_cards.PaymentCardEntity;

@SpringBootTest
class PaymentFailureIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Autowired
    private PaymentTransactionRepository transactionRepository;

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
        paymentCardRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @AfterEach
    void cleanDatabaseAfterTest() {
        cleanDatabase();
    }

    @Test
    void insufficientFundsCommitsFailedPaymentStateAndAuditTransaction() {
        OrderEntity order = orderRepository.saveAndFlush(
                new OrderEntity(
                        7L,
                        OrderStatus.CREATED,
                        new BigDecimal("100.00")
                )
        );
        PaymentCardEntity card = paymentCardRepository.saveAndFlush(
                new PaymentCardEntity(
                        7L,
                        "test-card-token",
                        "**** **** **** 4242",
                        new BigDecimal("10.00")
                )
        );

        assertThrows(
                InsufficientFundsException.class,
                () -> paymentService.payOrder(
                        order.getId(),
                        7L,
                        new PayOrderRequest(card.getId(), "failed-payment-key")
                )
        );

        OrderEntity failedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals(PaymentStatus.FAILED, failedOrder.getPaymentStatus());
        assertEquals(OrderStatus.CREATED, failedOrder.getStatus());
        assertEquals(
                TransactionStatus.FAILED,
                transactionRepository
                        .findByIdempotencyKey("failed-payment-key")
                        .orElseThrow()
                        .getStatus()
        );
    }
}
