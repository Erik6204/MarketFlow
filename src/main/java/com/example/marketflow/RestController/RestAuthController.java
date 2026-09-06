package com.example.marketflow.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.User.AuthenticatedUserDto;
import com.example.marketflow.User.LoginRequest;
import com.example.marketflow.User.RegisterRequest;
import com.example.marketflow.security.MarketFlowPrincipal;
import com.example.marketflow.security.SessionAuthenticationService;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class RestAuthController {

    private final AuthService authService;
    private final SessionAuthenticationService sessionAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserDto> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        MarketFlowPrincipal principal = sessionAuthenticationService.login(
                request,
                httpRequest,
                httpResponse
        );
        return ResponseEntity.ok(principal.toAuthenticatedUserDto());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        sessionAuthenticationService.logout(request, response, authentication);
        return ResponseEntity.noContent().build();
    }
}
