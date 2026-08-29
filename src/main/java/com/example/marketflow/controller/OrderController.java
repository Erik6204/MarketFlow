package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.service.CheckoutService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/account/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutService orderService;

    @PostMapping
    public String createOrder(HttpSession session) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            return "redirect:/login";
        }

        Long orderId = orderService.createOrder(buyerId);

        return "redirect:/account/orders/" + orderId;
    }
}
