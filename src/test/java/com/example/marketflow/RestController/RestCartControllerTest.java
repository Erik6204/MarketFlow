package com.example.marketflow.RestController;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.marketflow.cart.CartitemDto;
import com.example.marketflow.exception.CartItemNotFoundException;
import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.exception.InsufficientStockException;
import com.example.marketflow.exception.ProductNotFoundException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.service.CartService;

@ExtendWith(MockitoExtension.class)
class RestCartControllerTest {

    private static final Long BUYER_ID = 7L;

    @Mock
    private CartService cartService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestCartController controller = new RestCartController(cartService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    @Test
    void getCartItemsReturnsItems() throws Exception {
        when(cartService.getUserCartItems(BUYER_ID)).thenReturn(List.of(cartItem()));

        mockMvc.perform(get("/api/v1/cart/items").session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(25))
                .andExpect(jsonPath("$[0].productId").value(10))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].selected").value(true))
                .andExpect(jsonPath("$[0].subtotal").value(14980.00));
    }

    @Test
    void getCartItemsReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/cart/items"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"))
                .andExpect(jsonPath("$.path").value("/api/v1/cart/items"));

        verifyNoInteractions(cartService);
    }

    @Test
    void addProductToCartReturns204() throws Exception {
        mockMvc.perform(post("/api/v1/cart/items")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10}"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(cartService).addProductToCart(BUYER_ID, 10L);
    }

    @Test
    void addProductToCartReturns400ForInvalidProductId() throws Exception {
        mockMvc.perform(post("/api/v1/cart/items")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PRODUCT_ID"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("productId"));

        verifyNoInteractions(cartService);
    }

    @Test
    void addProductToCartReturns404WhenProductDoesNotExist() throws Exception {
        doThrow(new ProductNotFoundException(10L))
                .when(cartService).addProductToCart(BUYER_ID, 10L);

        mockMvc.perform(post("/api/v1/cart/items")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void addProductToCartReturns409WhenProductIsUnavailable() throws Exception {
        doThrow(new ProductUnavailableException())
                .when(cartService).addProductToCart(BUYER_ID, 10L);

        mockMvc.perform(post("/api/v1/cart/items")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":10}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("PRODUCT_UNAVAILABLE"));
    }

    @Test
    void updateQuantityReturns204() throws Exception {
        mockMvc.perform(patch("/api/v1/cart/items/{itemId}/quantity", 25)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":3}"))
                .andExpect(status().isNoContent());

        verify(cartService).updateCartItemQuantity(25L, BUYER_ID, 3);
    }

    @Test
    void updateQuantityReturns400ForInvalidQuantity() throws Exception {
        mockMvc.perform(patch("/api/v1/cart/items/{itemId}/quantity", 25)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_QUANTITY"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("quantity"));

        verifyNoInteractions(cartService);
    }

    @Test
    void updateQuantityReturns409ForInsufficientStock() throws Exception {
        doThrow(new InsufficientStockException())
                .when(cartService).updateCartItemQuantity(25L, BUYER_ID, 100);

        mockMvc.perform(patch("/api/v1/cart/items/{itemId}/quantity", 25)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":100}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_STOCK"));
    }

    @Test
    void updateSelectionReturns204AndUsesSessionBuyerId() throws Exception {
        mockMvc.perform(patch("/api/v1/cart/items/{itemId}/selection", 25)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"selected\":false}"))
                .andExpect(status().isNoContent());

        verify(cartService).changeCartItemSelection(25L, BUYER_ID, false);
    }

    @Test
    void updateSelectionReturns400WhenSelectedIsMissing() throws Exception {
        mockMvc.perform(patch("/api/v1/cart/items/{itemId}/selection", 25)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SELECTION"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("selected"));
    }

    @Test
    void deleteCartItemReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/cart/items/{itemId}", 25)
                        .session(authenticatedSession()))
                .andExpect(status().isNoContent());

        verify(cartService).removeCartItem(25L, BUYER_ID);
    }

    @Test
    void deleteCartItemReturns400ForNonPositiveItemId() throws Exception {
        mockMvc.perform(delete("/api/v1/cart/items/{itemId}", 0)
                        .session(authenticatedSession()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_CART_ITEM_ID"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("itemId"));

        verifyNoInteractions(cartService);
    }

    @Test
    void deleteCartItemReturns400ForInvalidItemIdFormat() throws Exception {
        mockMvc.perform(delete("/api/v1/cart/items/abc")
                        .session(authenticatedSession()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_CART_ITEM_ID"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("itemId"));

        verifyNoInteractions(cartService);
    }

    @Test
    void deleteCartItemReturns404WhenItemDoesNotExist() throws Exception {
        doThrow(new CartItemNotFoundException())
                .when(cartService).removeCartItem(25L, BUYER_ID);

        mockMvc.perform(delete("/api/v1/cart/items/{itemId}", 25)
                        .session(authenticatedSession()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CART_ITEM_NOT_FOUND"));
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", BUYER_ID);
        return session;
    }

    private CartitemDto cartItem() {
        return new CartitemDto(
                25L,
                10L,
                "Mechanical Keyboard",
                new BigDecimal("7490.00"),
                2,
                true,
                "https://example.com/images/keyboard.jpg",
                new BigDecimal("14980.00")
        );
    }
}
