package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.service.ProductService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/catalog")
    public String show(Model model){
        
        model.addAttribute(
                "products",
                productService.getAvailableProducts()
        );
        return "Page1";
    }

    @GetMapping("/products/{productId}")
    public String prod(Model model,@PathVariable("productId") Long id){
        model.addAttribute("product", productService.getProductById(id));
        return "Page2";
    }
}
