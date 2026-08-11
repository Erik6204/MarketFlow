package com.example.marketflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.products.ProductEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository rep;

    @Transactional(readOnly = true)
    public List<ProductEntity> getAvailableProducts() {

        return rep.findAllByActiveTrueAndQuantityGreaterThan(0);

    }

    @Transactional(readOnly = true)
    public ProductEntity getProductById(Long id) {
        return rep.findById(id).orElseThrow();
    }
}
