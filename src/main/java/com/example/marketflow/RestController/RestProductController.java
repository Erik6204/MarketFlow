package com.example.marketflow.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.marketflow.exception.InvalidProductId;
import com.example.marketflow.products.ProductDto;
import com.example.marketflow.service.ProductService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class RestProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>>  getProducts(){
        return ResponseEntity.ok().body(productService.getAvailableProducts());
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDto> getProductbyId(@PathVariable Long productId){
        if (productId<1)throw new InvalidProductId(productId);
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
