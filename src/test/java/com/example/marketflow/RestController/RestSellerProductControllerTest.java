package com.example.marketflow.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.marketflow.Seller.Service.SellerDashboardService;
import com.example.marketflow.Seller.Service.SellerProductService;
import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.exception.SellerAccessDeniedException;
import com.example.marketflow.products.CreateProductRequest;
import com.example.marketflow.products.SellerProductDto;
import com.example.marketflow.products.UpdateProductRequest;

@WebMvcTest(RestSellerProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalRestExceptionHandler.class)
class RestSellerProductControllerTest {

    private static final Long SELLER_ID = 3L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerProductService sellerProductService;

    @MockitoBean
    private SellerDashboardService sellerDashboardService;

    @Test
    void getSellerProductsReturnsProducts() throws Exception {
        when(sellerDashboardService.showallproductBySellerID(SELLER_ID))
                .thenReturn(List.of(productDto()));

        mockMvc.perform(get("/api/v1/seller/products").session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$[0].costPrice").value(6000.00))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(sellerDashboardService).showallproductBySellerID(SELLER_ID);
    }

    @Test
    void getSellerProductReturnsOwnedProduct() throws Exception {
        when(sellerProductService.getProductById(10L, SELLER_ID))
                .thenReturn(productDto());

        mockMvc.perform(get("/api/v1/seller/products/{productId}", 10L)
                        .session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.quantity").value(12));

        verify(sellerProductService).getProductById(10L, SELLER_ID);
    }

    @Test
    void createProductReturns201LocationAndProductId() throws Exception {
        when(sellerProductService.createProduct(
                any(CreateProductRequest.class),
                eq(SELLER_ID)
        )).thenReturn(10L);

        mockMvc.perform(post("/api/v1/seller/products")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Mechanical Keyboard",
                                  "description": "Keyboard with backlight",
                                  "price": 7490.00,
                                  "quantity": 12,
                                  "imageUrl": "https://example.com/keyboard.jpg"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/seller/products/10"))
                .andExpect(jsonPath("$.productId").value(10));

        ArgumentCaptor<CreateProductRequest> captor =
                ArgumentCaptor.forClass(CreateProductRequest.class);
        verify(sellerProductService).createProduct(captor.capture(), eq(SELLER_ID));
        assertEquals("Mechanical Keyboard", captor.getValue().name());
        assertEquals(12, captor.getValue().quantity());
    }

    @Test
    void updateProductReturns204AndPassesRequestToService() throws Exception {
        mockMvc.perform(patch("/api/v1/seller/products/{productId}", 10L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Keyboard",
                                  "price": 7990.00
                                }
                                """))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UpdateProductRequest> captor =
                ArgumentCaptor.forClass(UpdateProductRequest.class);
        verify(sellerProductService).updateProduct(
                eq(10L),
                eq(SELLER_ID),
                captor.capture()
        );
        assertEquals("Updated Keyboard", captor.getValue().getName());
    }

    @Test
    void updateAvailabilityEnablesProduct() throws Exception {
        mockMvc.perform(patch("/api/v1/seller/products/{productId}/availability", 10L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":true}"))
                .andExpect(status().isNoContent());

        verify(sellerProductService).EnableProduct(10L, SELLER_ID);
    }

    @Test
    void updateAvailabilityDisablesProduct() throws Exception {
        mockMvc.perform(patch("/api/v1/seller/products/{productId}/availability", 10L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isNoContent());

        verify(sellerProductService).DisableProduct(10L, SELLER_ID);
    }

    @Test
    void restockProductReturns204() throws Exception {
        mockMvc.perform(post("/api/v1/seller/products/{productId}/restock", 10L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":5}"))
                .andExpect(status().isNoContent());

        verify(sellerProductService).RestokeQuanityProduct(10L, SELLER_ID, 5);
    }

    @Test
    void getSellerProductsReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/seller/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));

        verifyNoInteractions(sellerDashboardService, sellerProductService);
    }

    @Test
    void getSellerProductReturns400ForInvalidProductId() throws Exception {
        mockMvc.perform(get("/api/v1/seller/products/{productId}", 0)
                        .session(authenticatedSession()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PRODUCT_ID"));

        verifyNoInteractions(sellerDashboardService, sellerProductService);
    }

    @Test
    void getSellerProductsReturns403WhenUserIsNotSeller() throws Exception {
        when(sellerDashboardService.showallproductBySellerID(SELLER_ID))
                .thenThrow(new SellerAccessDeniedException());

        mockMvc.perform(get("/api/v1/seller/products").session(authenticatedSession()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code").value("SELLER_ACCESS_DENIED"));
    }

    private SellerProductDto productDto() {
        return new SellerProductDto(
                10L,
                "Mechanical Keyboard",
                "Keyboard with backlight",
                new BigDecimal("7490.00"),
                12,
                "https://example.com/keyboard.jpg",
                new BigDecimal("6000.00"),
                true
        );
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", SELLER_ID);
        return session;
    }
}
