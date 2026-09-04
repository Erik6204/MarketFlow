package com.example.marketflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.marketflow.Repository.PaymentCardRepository;
import com.example.marketflow.payment_cards.AddPaymentCardRequest;
import com.example.marketflow.payment_cards.PaymentCardDto;
import com.example.marketflow.payment_cards.PaymentCardEntity;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @InjectMocks
    private PaymentCardService paymentCardService;

    @Test
    void addPaymentCardSavesProviderTokenButReturnsOnlySafeCardData() {
        AddPaymentCardRequest request = new AddPaymentCardRequest();
        request.setCardtoken("secret-provider-token");
        request.setMaskedNumber("**** **** **** 4242");
        request.setBalance(new BigDecimal("1000.00"));

        PaymentCardEntity savedCard = new PaymentCardEntity(
                7L,
                "secret-provider-token",
                "**** **** **** 4242",
                new BigDecimal("1000.00")
        );
        savedCard.setId(15L);
        when(paymentCardRepository.save(any(PaymentCardEntity.class)))
                .thenReturn(savedCard);

        PaymentCardDto result = paymentCardService.addPaymentCard(7L, request);

        assertEquals(15L, result.id());
        assertEquals("**** **** **** 4242", result.maskedNumber());
        assertEquals(0, new BigDecimal("1000.00").compareTo(result.balance()));

        ArgumentCaptor<PaymentCardEntity> captor =
                ArgumentCaptor.forClass(PaymentCardEntity.class);
        verify(paymentCardRepository).save(captor.capture());
        assertEquals(7L, captor.getValue().getUserid());
        assertEquals("secret-provider-token", captor.getValue().getCardtoken());
    }

    @Test
    void getUserPaymentCardsMapsRepositoryEntitiesToSafeDtos() {
        PaymentCardEntity card = new PaymentCardEntity(
                7L,
                "secret-provider-token",
                "**** **** **** 4242",
                new BigDecimal("1000.00")
        );
        card.setId(15L);
        when(paymentCardRepository.findAllByUserid(7L)).thenReturn(List.of(card));

        List<PaymentCardDto> result = paymentCardService.getUserPaymentCards(7L);

        assertEquals(1, result.size());
        assertEquals(15L, result.getFirst().id());
        assertEquals("**** **** **** 4242", result.getFirst().maskedNumber());
        verify(paymentCardRepository).findAllByUserid(7L);
    }
}
