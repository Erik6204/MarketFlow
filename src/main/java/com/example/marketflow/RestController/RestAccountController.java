package com.example.marketflow.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class RestAccountController {

    private final AuthService authService;

    private Long requireUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new AuthenticationRequiredException();
        }

        return userId;
    }

    @GetMapping
    public ResponseEntity<ShowUserDto> getAccount(HttpSession session) {
        Long userId = requireUserId(session);
        return ResponseEntity.ok(authService.getUserById(userId));
    }
}
