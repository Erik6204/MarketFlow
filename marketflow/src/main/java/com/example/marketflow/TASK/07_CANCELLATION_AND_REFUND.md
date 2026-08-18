# Задача 7. Отмена заказа и возврат денег

## Проблема

Пользователь должен иметь возможность отменить заказ или запросить возврат, но только на разрешённых этапах.

## Для чего это нужно

Отмена должна согласованно изменить заказ, деньги, остатки и финансовую историю.

## Возможные правила

```text
CREATED       можно отменить без возврата денег
PAID          можно отменить с возвратом
PACKING       отмена зависит от правила проекта
SHIPPED       обычная отмена запрещена
DELIVERED     создаётся отдельный запрос на возврат
CANCELLED     повторная отмена запрещена
```

## Что создать

```text
returns/ReturnRequestEntity.java
returns/ReturnStatus.java
Repository/ReturnRequestRepository.java
service/RefundService.java
controller/RefundController.java
exception/OrderCannotBeCancelledException.java
exception/RefundNotAllowedException.java
```

## Endpoint-ы

```text
POST /account/orders/{orderId}/cancel
POST /account/orders/{orderId}/returns
GET  /account/orders/{orderId}/returns
```

## Алгоритм возврата

В одной `@Transactional` операции:

1. Проверить пользователя, заказ и допустимый статус.
2. Изменить статус или создать заявку на возврат.
3. Вернуть деньги покупателю.
4. Скорректировать начисления продавца и владельца.
5. Создать финансовую запись `REFUND`.
6. При необходимости вернуть товар в остаток.

Заказы и финансовые записи нельзя физически удалять из базы.

