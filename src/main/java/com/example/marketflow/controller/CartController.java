package com.example.marketflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.marketflow.cart.CartitemDto;
import com.example.marketflow.service.CartService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart/items/{productId}")
    public String addProductToCart(Model model,@PathVariable("productId") Long productId,HttpSession session) {
        Long userId =
            (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        cartService.addProductToCart(userId, productId);

        return "redirect:/account/cart";
        
    }

    @GetMapping("/cart")
    public String ShowCartUser(Model model,HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<CartitemDto> cartItems =cartService.getUserCartItems(userId);

        model.addAttribute("cartItems", cartItems);

        return "showCart";
    }

    @PostMapping("/cart/items/{itemId}/quantity")
    public String UpdateCartItemQuantity(@PathVariable("itemId")Long itemId,
    @RequestParam("quantity") Integer quantity,HttpSession session){
        
        Long userId =(Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        cartService.updateCartItemQuantity(itemId, userId, quantity);
        return "redirect:/account/cart";
    }

    @PostMapping("/cart/items/{itemId}/select")
    public String ChangeCartItemSelection(@PathVariable("itemId") Long itemId,@RequestParam("selected") boolean selected,
    HttpSession session) 
    {
        Long userId =(Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }
        cartService.changeCartItemSelection(itemId, userId, selected);

        return "redirect:/account/cart";
    }

    @PostMapping("/cart/items/{itemId}/delete")
    public String RemoveCartItem(@PathVariable("itemId") Long itemId,HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        cartService.removeCartItem(itemId, userId);
        return "redirect:/account/cart";
    }
}
