package com.example.marketflow.security;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

import com.example.marketflow.User.LoginRequest;
import com.example.marketflow.exception.InvalidCredentialsException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class SessionAuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityContextRepository securityContextRepository;

    @Mock
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    @InjectMocks
    private SessionAuthenticationService authenticationService;

    @Test
    void shouldAuthenticateAndPersistSecurityContextInSession() {
        LoginRequest request = loginRequest();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpResponse = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Authentication authentication = mock(Authentication.class);
        MarketFlowPrincipal principal = mock(MarketFlowPrincipal.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getUserId()).thenReturn(7L);
        when(httpRequest.getSession()).thenReturn(session);

        MarketFlowPrincipal result = authenticationService.login(
                request,
                httpRequest,
                httpResponse
        );

        assertSame(principal, result);
        verify(sessionAuthenticationStrategy)
                .onAuthentication(authentication, httpRequest, httpResponse);
        verify(securityContextRepository)
                .saveContext(
                        any(SecurityContext.class),
                        eq(httpRequest),
                        eq(httpResponse)
                );
        verify(session).setAttribute("userId", 7L);
    }

    @Test
    void shouldReturnGenericErrorForInvalidCredentials() {
        LoginRequest request = loginRequest();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpResponse = mock(HttpServletResponse.class);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Wrong password"));

        assertThrows(
                InvalidCredentialsException.class,
                () -> authenticationService.login(request, httpRequest, httpResponse)
        );

        verify(securityContextRepository, never())
                .saveContext(any(), any(), any());
    }

    private LoginRequest loginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("buyer@example.com");
        request.setPassword("password123");
        return request;
    }
}
