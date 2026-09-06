package com.example.marketflow.security;

public record CsrfTokenDto(
        String headerName,
        String parameterName,
        String token
) {
}
