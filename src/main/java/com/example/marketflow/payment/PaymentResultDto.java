package com.example.marketflow.payment;

public record PaymentResultDto(Long orderId) {//PaymentResultDto нужен, чтобы после успешной оплаты вернуть клиенту
//  структурированный результат операции
}
