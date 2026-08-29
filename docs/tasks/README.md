# MarketFlow: план задач

Эта папка содержит технические задания для доведения MarketFlow до рабочего маркетплейса.

Рекомендуемый порядок выполнения:

1. [Завершение корзины](01-cart-completion.md)
2. [Кабинет продавца и товары](02-seller-cabinet.md)
3. [Создание заказа](03-order-creation.md)
4. [Защита товарных остатков](04-stock-concurrency.md)
5. [Оплата, комиссия и движение денег](05-payment-and-commission.md)
6. [Статусы заказа и работа продавца](06-order-lifecycle.md)
7. [Отмена и возврат](07-cancellation-and-refund.md)
8. [История заказов](08-order-history.md)
9. [Spring Security и роли](09-security-and-roles.md)
10. [RabbitMQ и события](10-rabbitmq-events.md)
11. [Тестирование и production](11-testing-and-production.md)

Основной поток приложения:

```text
Controller -> Service -> Repository -> Entity -> PostgreSQL
```

Сначала каждый сценарий реализуется синхронно через PostgreSQL. RabbitMQ, Redis и другие инфраструктурные инструменты добавляются после работающей основной бизнес-логики.

