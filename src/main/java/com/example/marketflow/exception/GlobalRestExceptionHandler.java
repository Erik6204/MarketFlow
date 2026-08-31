package com.example.marketflow.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;




@RestControllerAdvice(
        basePackages = "com.example.marketflow.RestController"
)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalRestExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFoundException(ProductNotFoundException exception,
        HttpServletRequest request){
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "PRODUCT_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<ApiError> handleProductUnavailableException(ProductUnavailableException exception,HttpServletRequest request){
        ApiError error=new ApiError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "PRODUCT_UNAVAILABLE",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(),
                null
        );
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(InvalidProductId.class)
    public ResponseEntity<ApiError> handleInvalidProductId(
            InvalidProductId exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PRODUCT_ID",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(
                        new ValidationError(
                                "productId",
                                "must be greater than or equal to 1"
                        )
                ),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidCartItemIdException.class)
    public ResponseEntity<ApiError> handleInvalidCartItemId(
            InvalidCartItemIdException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_CART_ITEM_ID",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(
                        new ValidationError(
                                "itemId",
                                "must be greater than or equal to 1"
                        )
                ),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    public ResponseEntity<ApiError> handleAuthenticationRequired(
            AuthenticationRequiredException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "AUTHENTICATION_REQUIRED",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ApiError> handleCartItemNotFound(
            CartItemNotFoundException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "CART_ITEM_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ApiError> handleInvalidQuantity(
            InvalidQuantityException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_QUANTITY",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(
                        new ValidationError(
                                "quantity",
                                "must be greater than or equal to 1"
                        )
                ),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleInsufficientStock(
            InsufficientStockException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "INSUFFICIENT_STOCK",
                exception.getMessage(),
                request.getRequestURI(),
                List.of(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        String field = exception.getName();
        boolean itemIdError = "itemId".equals(field);
        String code = itemIdError
                ? "INVALID_CART_ITEM_ID"
                : "INVALID_PRODUCT_ID";
        String message = itemIdError
                ? "Cart item ID must be a positive integer"
                : "Product ID must be a positive integer";

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                code,
                message,
                request.getRequestURI(),
                List.of(
                        new ValidationError(
                                field,
                                "must be a positive integer"
                        )
                ),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var fieldError = exception.getBindingResult().getFieldError();
        String field = fieldError == null ? "request" : fieldError.getField();
        String validationMessage = fieldError == null
                ? "invalid value"
                : fieldError.getDefaultMessage();

        String code;
        String message;

        if ("productId".equals(field)) {
            code = "INVALID_PRODUCT_ID";
            message = "Product ID must be a positive integer";
        } else if ("quantity".equals(field)) {
            code = "INVALID_QUANTITY";
            message = "Quantity must be greater than or equal to 1";
        } else if ("selected".equals(field)) {
            code = "INVALID_SELECTION";
            message = "Selected value is required";
        } else {
            code = "VALIDATION_ERROR";
            message = "Request validation failed";
        }

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                code,
                message,
                request.getRequestURI(),
                List.of(new ValidationError(field, validationMessage)),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
