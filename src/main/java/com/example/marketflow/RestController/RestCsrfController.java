package com.example.marketflow.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.security.CsrfTokenDto;

@RestController
@RequestMapping("/api/v1/csrf")
public class RestCsrfController {

    @GetMapping
    public ResponseEntity<CsrfTokenDto> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(new CsrfTokenDto(
                csrfToken.getHeaderName(),
                csrfToken.getParameterName(),
                csrfToken.getToken()
        ));
    }
}
