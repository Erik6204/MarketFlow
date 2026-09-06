package com.example.marketflow.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.Repository.userRoleRepository;
import com.example.marketflow.User.UserEntity;
import com.example.marketflow.User.UserStatus;

@ExtendWith(MockitoExtension.class)
class MarketFlowUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private userRoleRepository userRoleRepository;

    @InjectMocks
    private MarketFlowUserDetailsService userDetailsService;

    @Test
    void shouldLoadActiveUserWithGrantedRoles() {
        UserEntity user = user(7L, UserStatus.ACTIVE);
        when(userRepository.findByEmailIgnoreCase("seller@example.com"))
                .thenReturn(Optional.of(user));
        when(userRoleRepository.findRoleNamesByUserId(7L))
                .thenReturn(List.of("BUYER", "SELLER"));

        MarketFlowPrincipal principal = (MarketFlowPrincipal)
                userDetailsService.loadUserByUsername(" SELLER@EXAMPLE.COM ");

        assertTrue(principal.isEnabled());
        assertTrue(principal.isSeller());
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_BUYER".equals(authority.getAuthority())));
        verify(userRepository).findByEmailIgnoreCase("seller@example.com");
    }

    @Test
    void shouldDisableBlockedUser() {
        UserEntity user = user(8L, UserStatus.BLOCKED);
        when(userRepository.findByEmailIgnoreCase("blocked@example.com"))
                .thenReturn(Optional.of(user));
        when(userRoleRepository.findRoleNamesByUserId(8L))
                .thenReturn(List.of("BUYER"));

        MarketFlowPrincipal principal = (MarketFlowPrincipal)
                userDetailsService.loadUserByUsername("blocked@example.com");

        assertFalse(principal.isEnabled());
        assertFalse(principal.isAccountNonLocked());
    }

    @Test
    void shouldHideWhetherEmailExists() {
        when(userRepository.findByEmailIgnoreCase("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@example.com")
        );
    }

    private UserEntity user(Long id, UserStatus status) {
        UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(id);
        when(user.getEmail()).thenReturn("user@example.com");
        when(user.getPasswordHash()).thenReturn("password-hash");
        when(user.getDisplayName()).thenReturn("Ivan");
        when(user.getStatus()).thenReturn(status);
        return user;
    }
}
