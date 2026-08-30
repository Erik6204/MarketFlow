package com.example.marketflow.RestController;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.exception.ProductNotFoundException;
import com.example.marketflow.exception.ProductUnavailableException;
import com.example.marketflow.products.ProductDto;
import com.example.marketflow.service.ProductService;

@ExtendWith(MockitoExtension.class)
class RestProductControllerTest {

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestProductController controller = new RestProductController(productService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    @Test
    void getProductsReturnsAvailableProducts() throws Exception {
        ProductDto product = productDto();

        when(productService.getAvailableProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$[0].description").value("Compact mechanical keyboard with backlight"))
                .andExpect(jsonPath("$[0].price").value(7490.00))
                .andExpect(jsonPath("$[0].quantity").value(12))
                .andExpect(jsonPath("$[0].imageUrl").value("https://example.com/images/keyboard.jpg"));
    }

    @Test
    void getProductsReturnsEmptyArrayWhenNoProductsAreAvailable() throws Exception {
        when(productService.getAvailableProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void getProductByIdReturnsProduct() throws Exception {
        ProductDto product = productDto();

        when(productService.getProductById(10L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/{productId}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.description").value("Compact mechanical keyboard with backlight"))
                .andExpect(jsonPath("$.price").value(7490.00))
                .andExpect(jsonPath("$.quantity").value(12))
                .andExpect(jsonPath("$.imageUrl").value("https://example.com/images/keyboard.jpg"));
    }

    @Test
    void getProductByIdReturns400WhenIdIsNotPositive() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("INVALID_PRODUCT_ID"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/products/0"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("productId"))
                .andExpect(jsonPath("$.validationErrors[0].message")
                        .value("must be greater than or equal to 1"));

        verifyNoInteractions(productService);
    }

    @Test
    void getProductByIdReturns400WhenIdHasInvalidFormat() throws Exception {
        mockMvc.perform(get("/api/v1/products/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("INVALID_PRODUCT_ID"))
                .andExpect(jsonPath("$.message").value("Product ID must be a positive integer"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/abc"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("productId"))
                .andExpect(jsonPath("$.validationErrors[0].message")
                        .value("must be a positive integer"));

        verifyNoInteractions(productService);
    }

    @Test
    void getProductByIdReturns404WhenProductDoesNotExist() throws Exception {
        when(productService.getProductById(42L)).thenThrow(new ProductNotFoundException(42L));

        mockMvc.perform(get("/api/v1/products/{productId}", 42))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/products/42"))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors").isEmpty());
    }

    @Test
    void getProductByIdReturns409WhenProductIsUnavailable() throws Exception {
        when(productService.getProductById(42L)).thenThrow(new ProductUnavailableException());

        mockMvc.perform(get("/api/v1/products/{productId}", 42))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("PRODUCT_UNAVAILABLE"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/products/42"))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors").isEmpty());
    }

    private ProductDto productDto() {
        return new ProductDto(
                10L,
                "Mechanical Keyboard",
                "Compact mechanical keyboard with backlight",
                new BigDecimal("7490.00"),
                12,
                "https://example.com/images/keyboard.jpg"
        );
    }
}
