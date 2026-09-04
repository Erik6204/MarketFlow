package com.example.marketflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.PaymentCardRepository;
import com.example.marketflow.payment_cards.AddPaymentCardRequest;
import com.example.marketflow.payment_cards.PaymentCardEntity;
import com.example.marketflow.payment_cards.PaymentCardDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentCardService {
    private final PaymentCardRepository repository;

    @Transactional(readOnly = true)
    public List<PaymentCardDto> getUserPaymentCards(Long id) {
        return repository.findAllByUserid(id)
                .stream()
                .map(PaymentCardDto::from)
                .toList();
    }

    @Transactional
    public PaymentCardDto addPaymentCard(Long id, AddPaymentCardRequest dto) {
        PaymentCardEntity savedCard = repository.save(new PaymentCardEntity(
                id,
                dto.getCardtoken(),
                dto.getMaskedNumber(),
                dto.getBalance()
        ));

        return PaymentCardDto.from(savedCard);
    }
}
