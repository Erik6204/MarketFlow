package com.example.marketflow.exception;

public record ValidationError(
        String field,
        String message
) {
}