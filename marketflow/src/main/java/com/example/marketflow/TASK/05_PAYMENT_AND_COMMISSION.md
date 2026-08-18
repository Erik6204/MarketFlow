# Задача 5. Оплата, комиссия и движение денег

## Проблема

Одного изменения поля `balance` недостаточно. Нужна история каждого списания, начисления, комиссии и возврата.

## Для чего это нужно

Система должна уметь объяснить, почему баланс покупателя, продавца или владельца изменился.

## Что добавить через Flyway

Таблица `payment_transactions`:

```text
id
order_id
user_id
type
amount
status
idempotency_key
created_at
```

При необходимости добавить универсальную таблицу счетов `accounts` для продавцов и владельца.

## Enum

```text
TransactionType:
PAYMENT, SELLER_PAYOUT, PLATFORM_COMMISSION, REFUND

TransactionStatus:
CREATED, COMPLETED, FAILED, REFUNDED

PaymentStatus:
NOT_PAID, PROCESSING, PAID, FAILED, REFUNDED
```

## Что создать

```text
payment/PaymentTransactionEntity.java
payment/TransactionType.java
payment/TransactionStatus.java
payment/PaymentStatus.java
Repository/PaymentTransactionRepository.java
service/PaymentService.java
controller/PaymentController.java
exception/PaymentCardNotFoundException.java
exception/InsufficientFundsException.java
exception/PaymentAlreadyProcessedException.java
```

## Endpoint-ы

```text
GET  /account/orders/{orderId}/payment
POST /account/orders/{orderId}/payment
GET  /account/orders/{orderId}/payment/success
```

## Алгоритм оплаты

В одной `@Transactional` операции:

1. Проверить владельца заказа и отсутствие прошлой оплаты.
2. Проверить карту и её принадлежность покупателю.
3. Повторно проверить товары и остатки.
4. Списать сумму с покупателя.
5. Рассчитать комиссию платформы.
6. Начислить продавцам суммы без комиссии.
7. Начислить комиссию владельцу.
8. Сохранить финансовые операции.
9. Установить статус оплаты `PAID`.
10. Уменьшить остатки и удалить купленные позиции из корзины.

Для защиты от повторного POST использовать уникальный `idempotency_key`.

