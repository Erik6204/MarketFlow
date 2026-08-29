package com.example.marketflow.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Order.OrderEntity;
import com.example.marketflow.Order.OrderItemEntity;
import com.example.marketflow.Repository.OrderItemRepository;
import com.example.marketflow.Repository.OrderRepository;
import com.example.marketflow.Repository.PaymentCardRepository;
import com.example.marketflow.Repository.PaymentTransactionRepository;
import com.example.marketflow.Repository.WalletAccountRepository;
import com.example.marketflow.exception.InsufficientFundsException;
import com.example.marketflow.exception.OrderNotFoundException;
import com.example.marketflow.exception.OwnerWalletAccountNotFoundException;
import com.example.marketflow.exception.PaymentAlreadyProcessedException;
import com.example.marketflow.exception.PaymentCardNotFoundException;
import com.example.marketflow.exception.WalletAccountNotFoundException;
import com.example.marketflow.payment.PayOrderRequest;
import com.example.marketflow.payment.PaymentMapper;
import com.example.marketflow.payment.PaymentPageDto;
import com.example.marketflow.payment.PaymentStatus;
import com.example.marketflow.payment.PaymentTransactionEntity;
import com.example.marketflow.payment.TransactionStatus;
import com.example.marketflow.payment.TransactionType;
import com.example.marketflow.payment.WalletAccountEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository OR;
    private final PaymentCardRepository PCR;
    private final PaymentTransactionRepository PTR;
    private final OrderItemRepository OIR;
    private final WalletAccountRepository WAR;
    private static final BigDecimal PLATFORM_COMMISSION_RATE = new BigDecimal("0.10");

    @Transactional(readOnly = true)
    public PaymentPageDto getPaymentPage(Long orderId, Long buyerId) {
        OrderEntity order = OR.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return new PaymentPageDto(
                order.getId(),
                order.getTotalPrice(),
                order.getPaymentStatus(),
                PCR.findAllByUseridAndActiveTrue(buyerId)
                        .stream()
                        .map(PaymentMapper::convert)
                        .toList(),
                UUID.randomUUID().toString()
        );
    }

    @Transactional
    public Long payOrder(Long orderId, Long buyerId, PayOrderRequest request) {
        OrderEntity order = OR.findForPayment(orderId, buyerId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Optional<PaymentTransactionEntity> existingTransaction =
                PTR.findByIdempotencyKey(
                        request.idempotencyKey()
                );

        if (existingTransaction.isPresent()) {
            PaymentTransactionEntity transaction = existingTransaction.get();

            boolean samePayment =
                    transaction.getOrderId().equals(order.getId())
                    && transaction.getUserId().equals(buyerId)
                    && transaction.getType() == TransactionType.PAYMENT
                    && transaction.getStatus() == TransactionStatus.COMPLETED;

            if (samePayment) {
                return order.getId();
            }

            throw new PaymentAlreadyProcessedException();
        }

        if (order.getPaymentStatus() == PaymentStatus.NOT_PAID
                || order.getPaymentStatus() == PaymentStatus.FAILED) {

            PCR.findByIdAndUseridAndActiveTrue(
                    request.cardId(),
                    buyerId
            ).orElseThrow(PaymentCardNotFoundException::new);

            order.changePaymentStatus(
                    PaymentStatus.PROCESSING
            );

            int updatedRows = PCR.decreaseBalance(
                    request.cardId(),
                    buyerId,
                    order.getTotalPrice()
            );

            if (updatedRows != 1) {
                throw new InsufficientFundsException();
            }

            PTR.save(
                    new PaymentTransactionEntity(
                            order.getId(),
                            buyerId,
                            TransactionType.PAYMENT,
                            order.getTotalPrice(),
                            TransactionStatus.COMPLETED,
                            request.idempotencyKey()
                    )
            );

            List<OrderItemEntity> orderItems =
                    OIR.findAllByOrderId(order.getId());

            Map<Long, BigDecimal> totalsBySeller = orderItems.stream()
                    .collect(Collectors.toMap(
                            OrderItemEntity::getSellerId,
                            OrderItemEntity::getTotalPrice,
                            BigDecimal::add
                    ));

            BigDecimal totalCommission = BigDecimal.ZERO;

            for (Entry<Long, BigDecimal> entry : totalsBySeller.entrySet()) {

                BigDecimal commission = entry.getValue()
                        .multiply(PLATFORM_COMMISSION_RATE)
                        .setScale(2, RoundingMode.HALF_UP);

                BigDecimal sellerPayout = entry.getValue().subtract(commission);

                totalCommission = totalCommission.add(commission);

                int updatedWalletRows = WAR.increaseBalance(
                        entry.getKey(),
                        sellerPayout
                );

                if (updatedWalletRows != 1) {
                    throw new WalletAccountNotFoundException(entry.getKey());
                }

                PTR.save(
                        new PaymentTransactionEntity(
                                order.getId(),
                                entry.getKey(),
                                TransactionType.SELLER_PAYOUT,
                                sellerPayout,
                                TransactionStatus.COMPLETED,
                                request.idempotencyKey()
                                        + ":seller:"
                                        + entry.getKey()
                        )
                );
            }

            WalletAccountEntity ownerAccount = WAR.findOwnerAccount()
                    .orElseThrow(OwnerWalletAccountNotFoundException::new);

            if (totalCommission.signum() > 0) {
                int updatedOwnerRows = WAR.increaseBalance(
                        ownerAccount.getUserId(),
                        totalCommission
                );

                if (updatedOwnerRows != 1) {
                    throw new WalletAccountNotFoundException(
                            ownerAccount.getUserId()
                    );
                }

                PTR.save(
                        new PaymentTransactionEntity(
                                order.getId(),
                                ownerAccount.getUserId(),
                                TransactionType.PLATFORM_COMMISSION,
                                totalCommission,
                                TransactionStatus.COMPLETED,
                                request.idempotencyKey() + ":owner"
                        )
                );
            }

            order.changePaymentStatus(
                    PaymentStatus.PAID
            );

            return order.getId();
        }

        throw new PaymentAlreadyProcessedException();
    }
}
