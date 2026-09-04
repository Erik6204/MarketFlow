package com.example.marketflow.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.payment_cards.AddPaymentCardRequest;
import com.example.marketflow.payment_cards.PaymentCardDto;
import com.example.marketflow.service.PaymentCardService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment-cards")
@AllArgsConstructor
public class RestPaymentCardController {

    private final PaymentCardService paymentCardService;

    private Long requireBuyerId(HttpSession session) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            throw new AuthenticationRequiredException();
        }

        return buyerId;
    }

    @GetMapping
    public ResponseEntity<List<PaymentCardDto>> getPaymentCards(HttpSession session) {
        Long buyerId = requireBuyerId(session);
        return ResponseEntity.ok(paymentCardService.getUserPaymentCards(buyerId));
    }

    @PostMapping
    public ResponseEntity<PaymentCardDto> addPaymentCard(
            @Valid @RequestBody AddPaymentCardRequest request,
            HttpSession session
    ) {
        Long buyerId = requireBuyerId(session);
        PaymentCardDto card = paymentCardService.addPaymentCard(buyerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }
}
