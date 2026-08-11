package com.example.marketflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.PaymentCardRepository;
import com.example.marketflow.payment_cards.AddPaymentCardRequest;
import com.example.marketflow.payment_cards.PaymentCardEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentCardService {
    private final PaymentCardRepository repository;

    @Transactional(readOnly = true)
    public List<PaymentCardEntity> getUserPaymentCards(Long id) {
        return repository.findAllByUserid(id);
    }

    @Transactional
    public void addPaymentCard(Long id, AddPaymentCardRequest dto) {
        repository.save(new PaymentCardEntity(
                id,
                dto.getCardtoken(),
                dto.getMaskedNumber(),
                dto.getBalance()
        ));
    }
}
