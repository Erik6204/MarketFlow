package com.example.marketflow.Seller.Service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.ProductRepository;
import com.example.marketflow.exception.SellerAccessDeniedException;
import com.example.marketflow.products.ProductMapper;
import com.example.marketflow.products.SellerProductDto;
import com.example.marketflow.service.AuthService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class SellerDashboardService {
    private final ProductRepository repository;
    private final AuthService authService;
    
    @Transactional(readOnly=true)
    public List<SellerProductDto> showallproductBySellerID(Long sellerId){
        if (!Boolean.TRUE.equals(authService.isSeller(sellerId))) {
            throw new SellerAccessDeniedException();
        }

        return repository.findAllBySellerId(sellerId).stream().map(ProductMapper::toSellerDto).toList();
    }
}
