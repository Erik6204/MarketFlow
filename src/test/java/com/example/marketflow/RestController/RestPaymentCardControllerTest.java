package com.example.marketflow.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.payment_cards.AddPaymentCardRequest;
import com.example.marketflow.payment_cards.PaymentCardDto;
import com.example.marketflow.service.PaymentCardService;

@WebMvcTest(RestPaymentCardController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalRestExceptionHandler.class)
class RestPaymentCardControllerTest {

    private static final Long BUYER_ID = 7L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentCardService paymentCardService;

    @Test
    void getPaymentCardsReturnsSafeCardData() throws Exception {
        PaymentCardDto card = cardDto();
        when(paymentCardService.getUserPaymentCards(BUYER_ID))
                .thenReturn(List.of(card));

        mockMvc.perform(get("/api/v1/payment-cards").session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].maskedNumber").value("**** **** **** 4242"))
                .andExpect(jsonPath("$[0].balance").value(1000.00))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].cardtoken").doesNotExist());

        verify(paymentCardService).getUserPaymentCards(BUYER_ID);
    }

    @Test
    void addPaymentCardReturns201AndPassesRequestToService() throws Exception {
        when(paymentCardService.addPaymentCard(
                eq(BUYER_ID),
                any(AddPaymentCardRequest.class)
        )).thenReturn(cardDto());

        mockMvc.perform(post("/api/v1/payment-cards")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cardtoken": "safe-provider-token",
                                  "maskedNumber": "**** **** **** 4242",
                                  "balance": 1000.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 4242"))
                .andExpect(jsonPath("$.cardtoken").doesNotExist());

        ArgumentCaptor<AddPaymentCardRequest> captor =
                ArgumentCaptor.forClass(AddPaymentCardRequest.class);
        verify(paymentCardService).addPaymentCard(eq(BUYER_ID), captor.capture());

        assertEquals("safe-provider-token", captor.getValue().getCardtoken());
        assertEquals("**** **** **** 4242", captor.getValue().getMaskedNumber());
        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(captor.getValue().getBalance()));
    }

    @Test
    void addPaymentCardReturns400ForBlankToken() throws Exception {
        mockMvc.perform(post("/api/v1/payment-cards")
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cardtoken": "",
                                  "maskedNumber": "**** **** **** 4242",
                                  "balance": 1000.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[0].field").value("cardtoken"));

        verifyNoInteractions(paymentCardService);
    }

    @Test
    void getPaymentCardsReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/payment-cards"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));

        verifyNoInteractions(paymentCardService);
    }

    private PaymentCardDto cardDto() {
        return new PaymentCardDto(
                15L,
                "**** **** **** 4242",
                new BigDecimal("1000.00"),
                true
        );
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", BUYER_ID);
        return session;
    }
}
