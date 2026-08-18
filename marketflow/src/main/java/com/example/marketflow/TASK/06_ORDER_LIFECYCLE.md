# Задача 6. Статусы заказа и работа продавца

## Проблема

Один общий статус не способен одновременно описать состояние заказа, оплаты и действий продавца.

## Для чего это нужно

Покупатель должен видеть общее состояние покупки, а каждый продавец — состояние своей части заказа.

## Статусы

```text
OrderStatus:
CREATED, PROCESSING, COMPLETED, CANCELLED

PaymentStatus:
NOT_PAID, PROCESSING, PAID, FAILED, REFUNDED

FulfillmentStatus:
NEW, ACCEPTED, PACKING, READY_TO_SHIP, SHIPPED, DELIVERED, CANCELLED
```

## Несколько продавцов

Один заказ может содержать товары разных продавцов. Создать таблицу `seller_orders` или `fulfillments`:

```text
id
order_id
seller_id
status
created_at
shipped_at
delivered_at
```

## Что создать

```text
order/SellerOrderEntity.java
order/FulfillmentStatus.java
Repository/SellerOrderRepository.java
service/SellerOrderService.java
controller/SellerOrderController.java
exception/InvalidOrderStatusTransitionException.java
```

## Endpoint-ы продавца

```text
GET  /seller/orders
GET  /seller/orders/{sellerOrderId}
POST /seller/orders/{sellerOrderId}/accept
POST /seller/orders/{sellerOrderId}/packing
POST /seller/orders/{sellerOrderId}/ship
```

## Разрешённый поток

```text
NEW -> ACCEPTED -> PACKING -> READY_TO_SHIP -> SHIPPED -> DELIVERED
```

Нельзя перейти из `DELIVERED` обратно в `PACKING`. Проверка переходов должна находиться в Service.

Создать новую миграцию Flyway для расширения статусов и таблицы `seller_orders`. Применённую миграцию `V3` не изменять.

