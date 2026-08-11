package com.example.marketflow.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.products.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>{
    List<ProductEntity> findAllByActiveTrueAndQuantityGreaterThan(Integer quantity);
}
