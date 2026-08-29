package com.example.marketflow.products;

import java.math.BigDecimal;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDto toDto(ProductEntity product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getUrl()
        );
    }

    public static SellerProductDto toSellerDto(ProductEntity product){
        return new SellerProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getUrl(),
                (product.getPrice().multiply(new BigDecimal("0.80"))),
                product.getActive()
        );
    }

    public static ProductEntity toEntity(CreateProductRequest request, Long sellerId){
        return new ProductEntity(
                sellerId,
                request.name(),
                request.description(),
                request.price(),
                request.quantity(),
                request.imageUrl()
        );
    }

    public static UpdateProductRequest toUpdate(SellerProductDto dto){
        return new UpdateProductRequest(dto.name(), dto.description(), dto.price(), dto.imageUrl());
    }
}
