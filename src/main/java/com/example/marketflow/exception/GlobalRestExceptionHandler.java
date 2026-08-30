package com.example.marketflow.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException exception,
        HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PRODUCT_ID",
                "Product ID must be a positive integer",
                request.getRequestURI(),
                List.of(
                        new ValidationError(
                                exception.getName(),
                                "must be a positive integer"
                        )
                ),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
        }

}
