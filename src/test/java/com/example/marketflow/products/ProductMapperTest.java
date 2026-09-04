package com.example.marketflow.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductMapperTest {

    @Test
    void toSellerDtoCalculatesEightyPercentCostPrice() {
        ProductEntity product = new ProductEntity(
                7L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("1000.00"),
                10,
                "keyboard.jpg"
        );
        product.setId(20L);

        SellerProductDto result = ProductMapper.toSellerDto(product);

        assertEquals(20L, result.id());
        assertEquals(0, new BigDecimal("800.00").compareTo(result.costPrice()));
        assertTrue(result.active());
    }

    @Test
    void toEntityCopiesCreateRequestAndSellerId() {
        CreateProductRequest request = new CreateProductRequest(
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("1000.00"),
                10,
                "keyboard.jpg"
        );

        ProductEntity result = ProductMapper.toEntity(request, 7L);

        assertEquals(7L, result.getSellerId());
        assertEquals("Keyboard", result.getName());
        assertEquals(10, result.getQuantity());
        assertTrue(result.getActive());
    }
}
