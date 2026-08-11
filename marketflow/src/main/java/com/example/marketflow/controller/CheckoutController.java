package com.example.marketflow.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.service.CheckoutService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @GetMapping("/checkout")
    public String fourth(HttpSession session,Model model){
        Long userId =
            (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        BigDecimal total = checkoutService.calculateCartTotal(userId);

        model.addAttribute("sum", total);

        return "Page8";
    }
}
