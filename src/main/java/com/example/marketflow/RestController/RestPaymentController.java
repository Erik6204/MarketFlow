package com.example.marketflow.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.exception.InvalidOrderIdException;
import com.example.marketflow.payment.PayOrderRequest;
import com.example.marketflow.payment.PaymentPageDto;
import com.example.marketflow.payment.PaymentResultDto;
import com.example.marketflow.service.PaymentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class RestPaymentController {

    private final PaymentService paymentService;

    private Long requireBuyerId(HttpSession session) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            throw new AuthenticationRequiredException();
        }

        return buyerId;
    }

    private void validateOrderId(Long orderId) {
        if (orderId == null || orderId < 1) {
            throw new InvalidOrderIdException(orderId);
        }
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<PaymentPageDto> getPayment(
            @PathVariable Long orderId,
            HttpSession session
    ) {
        validateOrderId(orderId);
        Long buyerId = requireBuyerId(session);
        return ResponseEntity.ok(paymentService.getPaymentPage(orderId, buyerId));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<PaymentResultDto> payOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody PayOrderRequest request,
            HttpSession session
    ) {
        validateOrderId(orderId);
        Long buyerId = requireBuyerId(session);
        Long paidOrderId = paymentService.payOrder(orderId, buyerId, request);
        return ResponseEntity.ok(new PaymentResultDto(paidOrderId));
    }
}
