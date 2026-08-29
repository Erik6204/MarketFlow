package com.example.marketflow.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.payment_cards.PaymentCardEntity;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCardEntity,Long>{
    List<PaymentCardEntity> findAllByUserid(Long userid);
}
