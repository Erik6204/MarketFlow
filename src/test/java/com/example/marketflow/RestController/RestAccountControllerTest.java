package com.example.marketflow.RestController;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.User.UserStatus;
import com.example.marketflow.exception.GlobalRestExceptionHandler;
import com.example.marketflow.exception.UserNotFoundException;
import com.example.marketflow.service.AuthService;

@WebMvcTest(RestAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalRestExceptionHandler.class)
class RestAccountControllerTest {

    private static final Long USER_ID = 7L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void getAccountReturnsAuthenticatedUser() throws Exception {
        when(authService.getUserById(USER_ID)).thenReturn(new ShowUserDto(
                USER_ID,
                "buyer@example.com",
                UserStatus.ACTIVE,
                "Ivan"
        ));

        mockMvc.perform(get("/api/v1/account").session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.email").value("buyer@example.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.displayName").value("Ivan"));

        verify(authService).getUserById(USER_ID);
    }

    @Test
    void getAccountReturns401WithoutAuthenticatedSession() throws Exception {
        mockMvc.perform(get("/api/v1/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTHENTICATION_REQUIRED"))
                .andExpect(jsonPath("$.path").value("/api/v1/account"));

        verifyNoInteractions(authService);
    }

    @Test
    void getAccountReturns404WhenUserNoLongerExists() throws Exception {
        when(authService.getUserById(USER_ID))
                .thenThrow(new UserNotFoundException(USER_ID));

        mockMvc.perform(get("/api/v1/account").session(authenticatedSession()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
    }

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", USER_ID);
        return session;
    }
}
