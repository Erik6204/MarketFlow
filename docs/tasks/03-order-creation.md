# Задача 3. Создание заказа

## Проблема

`CheckoutService` сейчас только рассчитывает сумму выбранных товаров. Записи в `orders` и `order_items` не создаются.

## Для чего это нужно

После подтверждения checkout выбранные позиции должны превратиться в неизменяемую историю заказа.

## Что создать

```text
order/OrderEntity.java
order/OrderItemEntity.java
order/OrderStatus.java
order/OrderDto.java
order/OrderItemDto.java
order/OrderMapper.java
Repository/OrderRepository.java
Repository/OrderItemRepository.java
service/OrderService.java
controller/OrderController.java
exception/OrderNotFoundException.java
```

## Endpoint-ы

```text
GET  /account/checkout
POST /account/orders
GET  /account/orders/{orderId}
```

## Метод Service

```java
@Transactional
public Long createOrder(Long buyerId)
```

Алгоритм:

1. Получить выбранные `cart_items` покупателя.
2. Если список пустой, выбросить `NoSelectedCartItemsException`.
3. Загрузить товары и проверить существование, `active` и остатки.
4. Рассчитать итоговую сумму на сервере.
5. Создать `OrderEntity` со статусом `CREATED`.
6. Создать `OrderItemEntity` для каждой позиции.
7. Скопировать название, цену и изображение товара в `order_items`.
8. Вернуть `orderId`.

Данные товара копируются, чтобы позднее изменённая цена не изменила историю покупки.

## HTML

```text
orders/order-details.html
orders/order-created.html
```

