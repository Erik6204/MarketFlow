package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.service.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/catalog")
    public String showCatalog(
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "products",
                productService.getAvailableProducts()
        );

        return "showCatalog";
    }

    @GetMapping("/products/{productId}")
    public String showProduct(
            @PathVariable Long productId,
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "product",
                productService.getProductById(productId)
        );

        return "showProduct";
    }
}
