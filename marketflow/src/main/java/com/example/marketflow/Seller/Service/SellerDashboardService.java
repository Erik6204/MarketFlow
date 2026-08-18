package com.example.marketflow.Seller.Service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.products.ProductMapper;
import com.example.marketflow.products.SellerProductDto;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class SellerDashboardService {
    private final ProductRepository repository;
    
    @Transactional(readOnly=true)
    public List<SellerProductDto> showallproductBySellerID(Long sellerId){
        return repository.findAllBySellerId(sellerId).stream().map(ProductMapper::toSellerDto).toList();
    }
}
