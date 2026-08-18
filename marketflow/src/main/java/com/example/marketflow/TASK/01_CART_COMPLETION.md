# Задача 1. Завершение корзины

## Проблема

Основные операции корзины уже реализованы, но имя параметра выбора товара не синхронизировано между HTML и Controller. Также нужны проверки основных сценариев.

## Для чего это нужно

Корзина должна хранить товары конкретного покупателя, позволять менять количество, выбирать позиции для заказа и удалять их.

## Что исправить

- В `CartController` заменить `@RequestParam("choise")` на `@RequestParam("selected")`.
- Использовать `boolean selected`, чтобы в Service не передавался `null`.
- Убедиться, что `Page4.html` отправляет поле `selected`.
- Проверить обработку `NoSelectedCartItemsException`.
- Позже переименовать методы `pos`, `car`, `first`, `second`, `third` по смыслу.

## Endpoint-ы

```text
GET  /account/cart
POST /account/cart/items/{productId}
POST /account/cart/items/{itemId}/quantity
POST /account/cart/items/{itemId}/select
POST /account/cart/items/{itemId}/delete
```

## Проверяемые сценарии

- Новый товар создаёт одну позицию корзины.
- Повторное добавление увеличивает `quantity`, а не создаёт вторую строку.
- Количество меньше 1 вызывает `InvalidQuantityException`.
- Количество больше остатка вызывает `InsufficientStockException`.
- Нельзя изменить или удалить позицию другого пользователя.
- Пустая корзина отображается без ошибки.
- Checkout учитывает только позиции с `selected = true`.

## Файлы

```text
controller/CartController.java
service/CartService.java
Repository/CartItemRepository.java
cart/CartItemEntity.java
cart/CartitemDto.java
cart/CartitemMapper.java
templates/Page4.html
```

