# Задача 9. Spring Security и роли

## Проблема

Сейчас `HttpSession` вручную хранит `userId`, а Security разрешает все запросы. Пользователь может попытаться открыть seller или owner endpoint вручную.

## Для чего это нужно

Spring Security должен выполнять аутентификацию, хранить текущего пользователя и ограничивать доступ по ролям.

## Роли

```text
BUYER
SELLER
SELLER_MODERATOR
ANALYST
OWNER
```

Один пользователь может иметь несколько ролей через `user_roles`.

## Что создать или изменить

```text
security/CustomUserDetailsService.java
security/MarketFlowUserPrincipal.java
config/SecurityConfig.java
```

## Правила доступа

```text
/, /login, /register, /css/**        permitAll
/account/**                          authenticated
/seller/**                           hasRole("SELLER")
/moderator/**                        hasRole("SELLER_MODERATOR")
/analytics/**                        hasAnyRole("ANALYST", "OWNER")
/owner/**                            hasRole("OWNER")
```

## Использование в Controller

Вместо ручного `HttpSession` получать текущего пользователя через `Authentication` или:

```java
@AuthenticationPrincipal MarketFlowUserPrincipal principal
```

## Дополнительно

- Оставить BCrypt `PasswordEncoder`.
- Включить CSRF для HTML-форм и добавить CSRF-токены.
- Настроить страницы входа и `403`.
- Проверять принадлежность ресурсов в Service даже при наличии ролей.

