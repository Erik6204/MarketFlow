# MarketFlow Architecture

## Overview

MarketFlow is currently a modular monolith implemented as a server-rendered Spring Boot MVC application. Buyer and seller workflows share one deployable application and one PostgreSQL database while remaining separated at the controller and service levels.

The current structure favours a straightforward request flow:

```text
HTTP request -> Controller -> Service -> Repository -> PostgreSQL
                                      -> Thymeleaf view model
```

The architecture is intentionally kept as a monolith while the core marketplace rules are still evolving. Splitting the application into services before the domain boundaries and operational requirements are stable would add deployment and consistency complexity without a demonstrated benefit.

## Application layers

### Web layer

Controllers accept MVC requests, validate form DTOs, invoke application services, and return Thymeleaf views or redirects. Buyer-facing controllers live in the common `controller` package. Seller-specific endpoints are kept in the seller area.

### Application layer

Services implement use cases such as registration, catalogue browsing, cart management, checkout preparation, order creation, and seller product management. Transaction boundaries belong to this layer.

### Persistence layer

Spring Data JPA repositories persist users, roles, products, carts, orders, order items, and payment-card simulations. Flyway is the only supported mechanism for changing the database schema; Hibernate validates the schema at startup.

### Presentation layer

Thymeleaf templates render the current HTML interface. The planned REST API will be added as a separate boundary so that domain and application logic can be reused instead of duplicated.

## Important domain rules

- Seller operations must verify both seller authority and product ownership.
- Money is represented with `BigDecimal` in Java and fixed-precision `NUMERIC` columns in PostgreSQL.
- Order items store product name, price, seller, quantity, and image snapshots so historical orders do not change when a product is edited.
- Cart and product quantities must be positive and database constraints provide a second validation boundary.
- Schema changes are append-only Flyway migrations; applied migration files are never edited.

## Cross-cutting concerns

- Jakarta Validation protects request boundaries.
- Centralized exception handlers translate known failures into MVC error responses.
- Spring Boot Actuator exposes the application health endpoint.
- Secrets are supplied through environment variables and are never committed.

## Planned evolution

1. Stabilize the order, payment, security, and inventory workflows.
2. Introduce an OpenAPI-first REST boundary under `/api/v1`.
3. Add unit and integration tests, including PostgreSQL Testcontainers.
4. Add CI and container packaging.
5. Introduce Redis or RabbitMQ only for measured caching or asynchronous-event requirements.

## Diagrams

- [Application architecture](diagrams/architecture.puml)
- [Database ERD](diagrams/database-erd.puml)
