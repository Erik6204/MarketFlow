# Задача 8. История заказов

## Проблема

После создания и оплаты пользователь должен видеть свои заказы, а продавец — только связанные с ним части заказов.

## Для чего это нужно

История используется для просмотра товаров, сумм, статусов, оплаты, отмены и возврата.

## DTO

```text
OrderSummaryDto
OrderDetailsDto
OrderItemDto
SellerOrderDto
```

Entity напрямую в Controller и HTML не передавать.

## Repository

```java
List<OrderEntity> findAllByBuyerIdOrderByCreatedAtDesc(Long buyerId);
```

Для продавца понадобится запрос по `sellerId` в `seller_orders`.

## Endpoint-ы покупателя

```text
GET /account/orders
GET /account/orders/{orderId}
```

## Endpoint-ы продавца

```text
GET /seller/orders
GET /seller/orders/{sellerOrderId}
```

## Проверки

- Покупатель не может открыть чужой заказ.
- Продавец не может открыть часть заказа другого продавца.
- Показывается цена на момент покупки из `order_items`.
- Пустая история возвращает пустой список, а не исключение.

## HTML

```text
orders/order-list.html
orders/order-details.html
seller/order-list.html
seller/order-details.html
```

