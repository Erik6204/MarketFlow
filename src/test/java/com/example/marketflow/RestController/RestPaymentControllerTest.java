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
import com.example.marketflow.exception.InsufficientFundsException;
import com.example.marketflow.payment.PayOrderRequest;
import com.example.marketflow.payment.PaymentCardOptionDto;
import com.example.marketflow.payment.PaymentPageDto;
import com.example.marketflow.payment.PaymentStatus;
import com.example.marketflow.service.PaymentService;

@WebMvcTest(RestPaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalRestExceptionHandler.class)
class RestPaymentControllerTest {

    private static final Long BUYER_ID = 7L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void getPaymentReturnsPaymentPage() throws Exception {
        PaymentPageDto page = new PaymentPageDto(
                42L,
                new BigDecimal("14980.00"),
                PaymentStatus.NOT_PAID,
                List.of(new PaymentCardOptionDto(
                        15L,
                        "**** **** **** 4242",
                        new BigDecimal("20000.00")
                )),
                "payment-key-42"
        );
        when(paymentService.getPaymentPage(42L, BUYER_ID)).thenReturn(page);

        mockMvc.perform(get("/api/v1/orders/{orderId}/payment", 42L)
                        .session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(42))
                .andExpect(jsonPath("$.totalPrice").value(14980.00))
                .andExpect(jsonPath("$.paymentStatus").value("NOT_PAID"))
                .andExpect(jsonPath("$.cards[0].id").value(15))
                .andExpect(jsonPath("$.idempotencyKey").value("payment-key-42"));

        verify(paymentService).getPaymentPage(42L, BUYER_ID);
    }

    @Test
    void payOrderReturnsOrderIdAndPassesRequestToService() throws Exception {
        when(paymentService.payOrder(
                eq(42L),
                eq(BUYER_ID),
                any(PayOrderRequest.class)
        )).thenReturn(42L);

        mockMvc.perform(post("/api/v1/orders/{orderId}/payment", 42L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cardId": 15,
                                  "idempotencyKey": "payment-key-42"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(42));

        ArgumentCaptor<PayOrderRequest> captor =
                ArgumentCaptor.forClass(PayOrderRequest.class);
        verify(paymentService).payOrder(eq(42L), eq(BUYER_ID), captor.capture());

        assertEquals(15L, captor.getValue().cardId());
        assertEquals("payment-key-42", captor.getValue().idempotencyKey());
    }

    @Test
    void payOrderReturns400ForInvalidCardId() throws Exception {
        mockMvc.perform(post("/api/v1/orders/{orderId}/payment", 42L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cardId": 0,
                                  "idempotencyKey": "payment-key-42"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[0].field").value("cardId"));

        verifyNoInteractions(paymentService);
    }

    @Test
    void getPaymentReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/orders/{orderId}/payment", 42L))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));

        verifyNoInteractions(paymentService);
    }

    @Test
    void payOrderReturns409WhenCardHasInsufficientFunds() throws Exception {
        when(paymentService.payOrder(
                eq(42L),
                eq(BUYER_ID),
                any(PayOrderRequest.class)
        )).thenThrow(new InsufficientFundsException());

        mockMvc.perform(post("/api/v1/orders/{orderId}/payment", 42L)
                        .session(authenticatedSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cardId": 15,
                                  "idempotencyKey": "payment-key-42"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_FUNDS"));
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", BUYER_ID);
        return session;
    }
}
