package com.example.marketflow.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.checkout.CheckoutSummaryDto;
import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.service.CheckoutService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/checkout")
@AllArgsConstructor
public class RestCheckoutController {

    private final CheckoutService checkoutService;

    private Long requireBuyerId(HttpSession session) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            throw new AuthenticationRequiredException();
        }

        return buyerId;
    }

    @GetMapping
    public ResponseEntity<CheckoutSummaryDto> getCheckout(HttpSession session) {
        Long buyerId = requireBuyerId(session);
        return ResponseEntity.ok(
                new CheckoutSummaryDto(checkoutService.calculateCartTotal(buyerId))
        );
    }
}
