package com.example.marketflow.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.payment_cards.paymentcardsEntity;

@Repository
public interface paymentcardsRepository extends JpaRepository<paymentcardsEntity,Long>{
    List<paymentcardsEntity> findAllByUserid(Long userid);
}
