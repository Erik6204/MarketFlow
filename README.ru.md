# MarketFlow

[English version](README.md)

MarketFlow — находящийся в разработке многопродавцовый маркетплейс с server-rendered интерфейсом на Spring Boot. Проект предназначен для практики моделирования предметной области маркетплейса, транзакционной бизнес-логики, работы с PostgreSQL и разделения сценариев покупателя и продавца.

## Реализованные возможности

### Сценарий покупателя

- Регистрация и вход пользователя
- Каталог активных товаров с доступным остатком
- Страница отдельного товара
- Добавление товара в корзину, изменение количества, выбор и удаление позиций
- Расчёт стоимости выбранных позиций корзины
- Привязка и просмотр тестовых платёжных карт
- Начальное создание заказа со снимками позиций

### Сценарий продавца

- Кабинет продавца со списком собственных товаров
- Создание и просмотр товара
- Редактирование товара
- Включение и отключение товара
- Пополнение остатка
- Проверка принадлежности товара продавцу

### Общие компоненты

- Хранение данных в PostgreSQL через Spring Data JPA
- Версионирование схемы базы данных через Flyway
- Валидация запросов и централизованная обработка MVC-ошибок
- Thymeleaf-шаблоны для текущего server-rendered интерфейса
- Health endpoint Spring Boot Actuator
- Использование `BigDecimal` для денежных значений

## Стек технологий

- Java 21
- Spring Boot 4
- Spring MVC и Thymeleaf
- Spring Data JPA
- Компоненты Spring Security
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- JUnit 5

## Архитектура

Текущее приложение использует слоистую MVC-структуру:

```text
HTTP request
    -> Controller
    -> Service
    -> Repository
    -> PostgreSQL
    -> Thymeleaf view
```

Общие сценарии покупателя отделены от контроллеров и сервисов продавца. DTO используются на границах web- и service-слоёв, а JPA entities представляют сохраняемое состояние маркетплейса.

## Основные таблицы

| Таблица | Назначение |
| --- | --- |
| `users` | Зарегистрированные пользователи |
| `roles` | Поддерживаемые роли аккаунтов |
| `user_roles` | Связи пользователей и ролей |
| `products` | Товары продавцов, цены, доступность и остатки |
| `cart_items` | Состояние корзин покупателей |
| `orders` | Покупатель, статус, сумма и время заказа |
| `order_items` | Снимки товаров на момент создания заказа |
| `payment_cards` | Тестовые платёжные карты пользователей |
| `flyway_schema_history` | История применённых миграций |

## Документация

- [Идея проекта](docs/project-idea.md)
- [Техническое задание](docs/technical-specification.md)
- [Архитектура](docs/architecture.md)
- [База данных](docs/database.md)
- [Разработка API](docs/api-development.md)
- [Задания по реализации](docs/tasks/README.md)

## Структура проекта

```text
MarketFlow/
├── .mvn/
├── docs/
│   ├── architecture.md
│   ├── api-development.md
│   ├── database.md
│   ├── diagrams/
│   └── tasks/
├── src/
├── .editorconfig
├── .env.example
├── LICENSE
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── README.ru.md
```

## Локальный запуск

### Требования

- Java 21
- PostgreSQL
- Git

Создай базу данных PostgreSQL, скопируй `.env.example` в `.env` и укажи локальные данные подключения:

```env
DB_URL=jdbc:postgresql://localhost:5432/marketflow
DB_USERNAME=postgres
DB_PASSWORD=change_me
```

Не добавляй настоящий `.env` в Git.

Запуск на Linux или macOS:

```bash
./mvnw spring-boot:run
```

Запуск на Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Flyway применит миграции при старте. Приложение будет доступно по адресу `http://localhost:8080`.

## Запуск тестов

Linux или macOS:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

## Основные маршруты

| Метод | Путь | Назначение |
| --- | --- | --- |
| `GET` | `/` | Стартовая страница |
| `GET`, `POST` | `/register` | Регистрация пользователя |
| `GET`, `POST` | `/login` | Вход пользователя |
| `GET` | `/account/catalog` | Каталог товаров |
| `GET` | `/account/products/{productId}` | Страница товара |
| `GET` | `/account/cart` | Корзина покупателя |
| `GET` | `/account/checkout` | Итоговая информация перед заказом |
| `POST` | `/account/orders` | Создание заказа из выбранных позиций |
| `GET` | `/seller/account` | Кабинет продавца |
| `GET` | `/seller/products/new` | Форма создания товара |
| `POST` | `/seller/products` | Создание товара продавцом |
| `GET`, `POST` | `/seller/products/{productId}/edit` | Редактирование товара |
| `POST` | `/seller/products/{productId}/restock` | Увеличение остатка товара |
| `GET` | `/actuator/health` | Состояние приложения |

## Текущее состояние

Репозиторий находится в активной разработке. В публичной ветке реализована MVC-основа маркетплейса, корзина, сценарии продавца и начальное создание заказа. Сейчас в ней **нет** production-интеграции платежей, Redis, RabbitMQ, Docker, OpenAPI и завершённой политики безопасности. Эти технологии будут добавляться только после реализации и проверки соответствующих сценариев.

## План развития

- Завершить жизненный цикл заказа и платёжную модель
- Заменить временную session-аутентификацию полноценной политикой Spring Security
- Добавить REST endpoints и OpenAPI-контракт
- Добавить unit- и integration-тесты, включая Testcontainers
- Добавить Docker и CI через GitHub Actions
- Подключить Redis и RabbitMQ для обоснованных сценариев кеширования и асинхронных событий

