package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.products.productsEntity;

@Repository
public interface productsRepository extends JpaRepository<productsEntity,Long>{
}
