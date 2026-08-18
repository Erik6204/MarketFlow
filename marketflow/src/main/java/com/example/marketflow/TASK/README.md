# MarketFlow: план задач

Эта папка содержит технические задания для доведения MarketFlow до рабочего маркетплейса.

Рекомендуемый порядок выполнения:

1. [Завершение корзины](01_CART_COMPLETION.md)
2. [Кабинет продавца и товары](02_SELLER_CABINET.md)
3. [Создание заказа](03_ORDER_CREATION.md)
4. [Защита товарных остатков](04_STOCK_CONCURRENCY.md)
5. [Оплата, комиссия и движение денег](05_PAYMENT_AND_COMMISSION.md)
6. [Статусы заказа и работа продавца](06_ORDER_LIFECYCLE.md)
7. [Отмена и возврат](07_CANCELLATION_AND_REFUND.md)
8. [История заказов](08_ORDER_HISTORY.md)
9. [Spring Security и роли](09_SECURITY_AND_ROLES.md)
10. [RabbitMQ и события](10_RABBITMQ_EVENTS.md)
11. [Тестирование и production](11_TESTING_AND_PRODUCTION.md)

Основной поток приложения:

```text
Controller -> Service -> Repository -> Entity -> PostgreSQL
```

Сначала каждый сценарий реализуется синхронно через PostgreSQL. RabbitMQ, Redis и другие инфраструктурные инструменты добавляются после работающей основной бизнес-логики.

