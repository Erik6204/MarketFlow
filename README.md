# MarketFlow

[Русская версия](README.ru.md)

MarketFlow is a work-in-progress multi-vendor marketplace built as a server-rendered Spring Boot application. The project focuses on marketplace domain modelling, transactional business logic, PostgreSQL persistence, and the separation of buyer and seller workflows.

## Current functionality

### Buyer workflow

- User registration and login
- Product catalogue containing active products with available stock
- Product details page
- Shopping-cart item creation, quantity updates, selection, and removal
- Cart-total calculation for selected items
- Payment-card attachment and listing
- Initial order creation with order-item snapshots

### Seller workflow

- Seller dashboard with the seller's own products
- Product creation and details
- Product editing
- Product activation and deactivation
- Inventory restocking
- Ownership checks for seller operations

### Platform concerns

- PostgreSQL persistence through Spring Data JPA
- Versioned database schema with Flyway
- Request validation and centralized MVC exception handling
- Thymeleaf templates for the current server-rendered UI
- Spring Boot Actuator health endpoint
- Monetary values represented with `BigDecimal`

## Tech stack

- Java 21
- Spring Boot 4
- Spring MVC and Thymeleaf
- Spring Data JPA
- Spring Security components
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- JUnit 5

## Architecture

The current application follows a layered MVC structure:

```text
HTTP request
    -> Controller
    -> Service
    -> Repository
    -> PostgreSQL
    -> Thymeleaf view
```

The codebase separates common buyer flows from seller-specific controllers and services. DTOs are used at web and service boundaries, while JPA entities represent persisted marketplace state.

## Main domain tables

| Table | Purpose |
| --- | --- |
| `users` | Registered marketplace users |
| `roles` | Supported account roles |
| `user_roles` | User-to-role assignments |
| `products` | Seller products, prices, availability, and stock |
| `cart_items` | Buyer shopping-cart state |
| `orders` | Order header, buyer, status, total, and timestamps |
| `order_items` | Product snapshots stored at order-creation time |
| `payment_cards` | Simulated user payment cards |
| `flyway_schema_history` | Applied migration history |

## Documentation

- [Project idea](docs/project-idea.md)
- [Technical specification](docs/technical-specification.md)
- [Architecture](docs/architecture.md)
- [Database](docs/database.md)
- [API development](docs/api-development.md)
- [Implementation tasks](docs/tasks/README.md)

## Project structure

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

## Running locally

### Requirements

- Java 21
- PostgreSQL
- Git

Create a PostgreSQL database, copy `.env.example` to `.env`, and provide your local credentials:

```env
DB_URL=jdbc:postgresql://localhost:5432/marketflow
DB_USERNAME=postgres
DB_PASSWORD=change_me
```

Never commit the real `.env` file.

Run on Linux or macOS:

```bash
./mvnw spring-boot:run
```

Run on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Flyway will apply the database migrations at startup. The application will be available at `http://localhost:8080`.

## Running tests

Linux or macOS:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

## Main routes

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/` | Start page |
| `GET`, `POST` | `/register` | User registration |
| `GET`, `POST` | `/login` | User login |
| `GET` | `/account/catalog` | Product catalogue |
| `GET` | `/account/products/{productId}` | Product details |
| `GET` | `/account/cart` | Buyer cart |
| `GET` | `/account/checkout` | Checkout summary |
| `POST` | `/account/orders` | Create an order from selected cart items |
| `GET` | `/seller/account` | Seller dashboard |
| `GET` | `/seller/products/new` | Product-creation form |
| `POST` | `/seller/products` | Create a seller product |
| `GET`, `POST` | `/seller/products/{productId}/edit` | Edit a seller product |
| `POST` | `/seller/products/{productId}/restock` | Increase product stock |
| `GET` | `/actuator/health` | Application health |

## Current status

This repository is under active development. The public branch contains the MVC marketplace foundation, cart and seller workflows, and initial order creation. It does **not** currently include production-ready payment integration, Redis, RabbitMQ, Docker packaging, OpenAPI, or a complete security policy. Those technologies will be added only when their use cases are implemented and verified.

## Roadmap

- Complete the order lifecycle and payment model
- Replace temporary session-based authentication with a complete Spring Security policy
- Add REST endpoints and an OpenAPI contract
- Add unit and integration tests, including Testcontainers
- Add Docker packaging and GitHub Actions CI
- Introduce Redis and RabbitMQ for justified caching and asynchronous-event scenarios

