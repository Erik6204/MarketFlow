package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.cart.cartitemsentity;

@Repository
public  interface cartitemsRepository extends JpaRepository<cartitemsentity,Long> {
}
