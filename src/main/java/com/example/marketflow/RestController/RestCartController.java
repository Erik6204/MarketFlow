package com.example.marketflow.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.cart.AddCartItemRequest;
import com.example.marketflow.cart.CartitemDto;
import com.example.marketflow.cart.UpdateCartItemQuantityRequest;
import com.example.marketflow.cart.UpdateCartItemSelectionRequest;
import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.exception.InvalidCartItemIdException;
import com.example.marketflow.service.CartService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class RestCartController {
    private final CartService cartService;

    private void validateItemId(Long itemId) {
        if (itemId == null || itemId < 1) {
            throw new InvalidCartItemIdException(itemId);
        }
    }

    private Long requireBuyerId(HttpSession session) {
        Long buyerId = (Long) session.getAttribute("userId");

        if (buyerId == null) {
            throw new AuthenticationRequiredException();
        }

        return buyerId;
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartitemDto>> getCartItems(HttpSession session){
        Long buyerId = requireBuyerId(session);
        return ResponseEntity.ok(cartService.getUserCartItems(buyerId));
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addCartItemForUser(@Valid @RequestBody AddCartItemRequest request,HttpSession session){
        Long buyerId = requireBuyerId(session);
        cartService.addProductToCart(buyerId, request.productId());
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/items/{itemId}/quantity")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long itemId,
        @Valid @RequestBody UpdateCartItemQuantityRequest request,HttpSession session){
        validateItemId(itemId);
        Long buyerId = requireBuyerId(session);
        cartService.updateCartItemQuantity(itemId, buyerId, request.quantity());
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/items/{itemId}/selection")
    public ResponseEntity<Void> updateCartItemSelection(@PathVariable Long itemId,
        @Valid @RequestBody UpdateCartItemSelectionRequest request,HttpSession session){
        validateItemId(itemId);
        Long buyerId = requireBuyerId(session);
        cartService.changeCartItemSelection(itemId, buyerId, request.selected());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long itemId,HttpSession session){
        validateItemId(itemId);
        Long buyerId = requireBuyerId(session);
        cartService.removeCartItem(itemId, buyerId);
        return ResponseEntity.noContent().build();
    }

}
