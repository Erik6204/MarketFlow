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
import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class RestAuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserDto> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        ShowUserDto user = authService.authenticate(request);
        boolean seller = Boolean.TRUE.equals(authService.isSeller(user.getId()));

        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok(AuthenticatedUserDto.from(user, seller));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
