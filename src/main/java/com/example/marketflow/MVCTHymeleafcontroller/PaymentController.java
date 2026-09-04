package com.example.marketflow.MVCTHymeleafcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.payment.PayOrderRequest;
import com.example.marketflow.service.PaymentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account/orders")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService PS;

    @GetMapping("/{orderId}/payment")
    public String showPaymentPage(
            @PathVariable Long orderId,
            HttpSession session,
            Model model
    ) {
        Long buyerId = (Long) session.getAttribute("userId");
        if (buyerId == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "payment",
                PS.getPaymentPage(orderId, buyerId)
        );

        return "orders/payment";
    }

    @PostMapping("/{orderId}/payment")
    public String payOrder(
            @PathVariable Long orderId,
            @Valid @ModelAttribute PayOrderRequest request,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "payment",
                    PS.getPaymentPage(orderId, buyerId)
            );

            return "orders/payment";
        }

        PS.payOrder(orderId, buyerId, request);

        return "redirect:/account/orders/" + orderId;
    }
}
