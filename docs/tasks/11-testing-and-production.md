# Задача 11. Тестирование и подготовка к production

## Проблема

Ручная проверка интерфейса не доказывает корректность конкурентных операций, транзакций, доступа и обработки ошибок.

## Для чего это нужно

Тесты защищают бизнес-правила от случайных поломок при дальнейшем рефакторинге.

## Unit-тесты Service

```text
CartServiceTest
OrderServiceTest
PaymentServiceTest
SellerProductServiceTest
RefundServiceTest
```

Проверить успешные сценарии, отсутствующие сущности, недостаток товара и денег, запрещённые статусы, доступ к чужим ресурсам и повторную оплату.

## MVC-тесты

Через MockMvc проверить:

```text
маршруты и HTTP-методы;
redirect;
валидацию форм;
статусы ошибок;
данные в Model;
ограничения Spring Security.
```

## Интеграционные тесты

- Использовать PostgreSQL через Testcontainers.
- Проверять Flyway migrations.
- Проверить откат `@Transactional` при ошибке оплаты.
- Проверить конкурентную покупку последнего товара.
- Для RabbitMQ использовать Testcontainers или тестовый профиль.

## Production-подготовка

```text
единый GlobalExceptionHandler;
структурированные логи;
Actuator health/readiness;
profiles local/test/prod;
environment variables для секретов;
Dockerfile и docker-compose;
OpenAPI-документация;
pagination для каталогов и истории;
индексы PostgreSQL;
метрики и аудит критичных операций.
```

## Критерий завершения

Проект собирается одной Maven-командой, миграции применяются автоматически, ключевые сценарии покрыты тестами, а приложение запускается вместе с PostgreSQL и RabbitMQ через Docker Compose.

