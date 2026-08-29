# API Development

## Current state

MarketFlow currently exposes a server-rendered Spring MVC interface. The REST API is planned but is not yet part of the public contract. This document defines how the REST boundary should be introduced without duplicating business logic.

## Contract-first workflow

1. Describe one complete use case in `openapi/marketflow-api.yaml`.
2. Validate the document with Swagger Editor and an automated build check.
3. Review paths, schemas, status codes, and error responses before implementation.
4. Generate Spring API interfaces and transport DTOs with OpenAPI Generator.
5. Implement the generated interfaces in controllers.
6. Keep business rules in application services rather than generated code.
7. Publish the contract and Swagger UI through the application.

Generated files must not be edited manually. Contract changes start in the OpenAPI document and generated sources are replaced during the build.

## URL conventions

- REST endpoints use the `/api/v1` prefix.
- Collection resources use plural nouns, for example `/api/v1/products`.
- Resource identifiers are path parameters, for example `/api/v1/products/{productId}`.
- Filtering and pagination use query parameters.
- Commands that create non-idempotent financial effects require an idempotency key.

## HTTP conventions

| Operation | Expected status |
| --- | --- |
| Successful read | `200 OK` |
| Successful creation | `201 Created` |
| Successful update without response body | `204 No Content` |
| Validation failure | `400 Bad Request` |
| Missing or invalid authentication | `401 Unauthorized` |
| Insufficient permissions | `403 Forbidden` |
| Missing resource | `404 Not Found` |
| State or uniqueness conflict | `409 Conflict` |

## Error contract

All REST errors should use one stable schema containing at least:

```json
{
  "code": "PRODUCT_NOT_FOUND",
  "message": "Product was not found",
  "status": 404,
  "path": "/api/v1/products/42",
  "timestamp": "2026-08-29T12:00:00Z"
}
```

Validation failures may additionally contain field-level errors.

## Initial API slice

The first contract should remain small and cover catalogue reads:

```http
GET /api/v1/products
GET /api/v1/products/{productId}
```

This slice is read-only, easy to verify, and reuses the existing product service. Order and payment operations should be added only after their domain workflows are stable.
