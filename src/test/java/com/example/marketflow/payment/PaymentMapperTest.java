package com.example.marketflow.payment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.marketflow.payment_cards.PaymentCardEntity;

public class PaymentMapperTest {
    @Test
    void testconvert(){
        PaymentCardEntity entity=new PaymentCardEntity(5L,
                "secret-card-token",
                "**** **** **** 4242",
                new BigDecimal("1000.00"));
        
        entity.setId(10L);
        
        PaymentCardOptionDto dto=PaymentMapper.convert(entity);

        assertEquals(10L, dto.id());
        assertEquals("**** **** **** 4242", dto.maskedNumber());
        assertEquals(new BigDecimal("1000.00"), dto.balance());
    }
}
