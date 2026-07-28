package com.example.marketflow.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.cart.cartitemsentity;

@Repository
public  interface cartitemsRepository extends JpaRepository<cartitemsentity,Long> {
    List<cartitemsentity> findAllByBuyerid(Long buyerId);
    Optional<cartitemsentity> findByIdAndBuyerid(Long id,Long buyerid);
    List<cartitemsentity> findAllByBuyeridAndSelectedTrue(Long buyerid);
    Optional<cartitemsentity> findByBuyeridAndProductid(Long buyerid,Long productid);
}
