package com.example.marketflow.RestController;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.exception.NoSelectedCartItemsException;
import com.example.marketflow.service.CheckoutService;

@WebMvcTest(RestCheckoutController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalRestExceptionHandler.class)
class RestCheckoutControllerTest {

    private static final Long BUYER_ID = 7L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckoutService checkoutService;

    @Test
    void getCheckoutReturnsCalculatedTotal() throws Exception {
        when(checkoutService.calculateCartTotal(BUYER_ID))
                .thenReturn(new BigDecimal("14980.00"));

        mockMvc.perform(get("/api/v1/checkout").session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(14980.00));

        verify(checkoutService).calculateCartTotal(BUYER_ID);
    }

    @Test
    void getCheckoutReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/checkout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"));

        verifyNoInteractions(checkoutService);
    }

    @Test
    void getCheckoutReturns409WhenNoCartItemsAreSelected() throws Exception {
        when(checkoutService.calculateCartTotal(BUYER_ID))
                .thenThrow(new NoSelectedCartItemsException());

        mockMvc.perform(get("/api/v1/checkout").session(authenticatedSession()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("NO_SELECTED_CART_ITEMS"));
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", BUYER_ID);
        return session;
    }
}
