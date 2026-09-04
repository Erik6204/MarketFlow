package com.example.marketflow.RestController;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.Seller.Service.SellerDashboardService;
import com.example.marketflow.Seller.Service.SellerProductService;
import com.example.marketflow.exception.AuthenticationRequiredException;
import com.example.marketflow.exception.InvalidProductId;
import com.example.marketflow.products.CreateProductRequest;
import com.example.marketflow.products.CreatedProductDto;
import com.example.marketflow.products.RestockProductRequest;
import com.example.marketflow.products.SellerProductDto;
import com.example.marketflow.products.UpdateProductAvailabilityRequest;
import com.example.marketflow.products.UpdateProductRequest;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/seller/products")
@AllArgsConstructor
public class RestSellerProductController {

    private final SellerProductService sellerProductService;
    private final SellerDashboardService sellerDashboardService;

    private Long requireSellerId(HttpSession session) {
        Long sellerId = (Long) session.getAttribute("userId");

        if (sellerId == null) {
            throw new AuthenticationRequiredException();
        }

        return sellerId;
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId < 1) {
            throw new InvalidProductId(productId);
        }
    }

    @GetMapping
    public ResponseEntity<List<SellerProductDto>> getSellerProducts(HttpSession session) {
        Long sellerId = requireSellerId(session);
        return ResponseEntity.ok(
                sellerDashboardService.showallproductBySellerID(sellerId)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<SellerProductDto> getSellerProduct(
            @PathVariable Long productId,
            HttpSession session
    ) {
        validateProductId(productId);
        Long sellerId = requireSellerId(session);
        return ResponseEntity.ok(
                sellerProductService.getProductById(productId, sellerId)
        );
    }

    @PostMapping
    public ResponseEntity<CreatedProductDto> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            HttpSession session
    ) {
        Long sellerId = requireSellerId(session);
        Long productId = sellerProductService.createProduct(request, sellerId);

        return ResponseEntity
                .created(URI.create("/api/v1/seller/products/" + productId))
                .body(new CreatedProductDto(productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request,
            HttpSession session
    ) {
        validateProductId(productId);
        Long sellerId = requireSellerId(session);
        sellerProductService.updateProduct(productId, sellerId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}/availability")
    public ResponseEntity<Void> updateProductAvailability(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductAvailabilityRequest request,
            HttpSession session
    ) {
        validateProductId(productId);
        Long sellerId = requireSellerId(session);

        if (Boolean.TRUE.equals(request.active())) {
            sellerProductService.EnableProduct(productId, sellerId);
        } else {
            sellerProductService.DisableProduct(productId, sellerId);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/restock")
    public ResponseEntity<Void> restockProduct(
            @PathVariable Long productId,
            @Valid @RequestBody RestockProductRequest request,
            HttpSession session
    ) {
        validateProductId(productId);
        Long sellerId = requireSellerId(session);
        sellerProductService.RestokeQuanityProduct(
                productId,
                sellerId,
                request.amount()
        );
        return ResponseEntity.noContent().build();
    }
}
