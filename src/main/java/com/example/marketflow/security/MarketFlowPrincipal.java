package com.example.marketflow.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.marketflow.User.AuthenticatedUserDto;
import com.example.marketflow.User.UserEntity;
import com.example.marketflow.User.UserStatus;

public final class MarketFlowPrincipal implements UserDetails, Serializable {
//Представляет авторизованного пользователя внутри Spring Security.
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String email;
    private final String passwordHash;
    private final String displayName;
    private final UserStatus status;
    private final List<GrantedAuthority> authorities;

    public MarketFlowPrincipal(
            UserEntity user,
            Collection<? extends GrantedAuthority> authorities //ограничение сверху
    ) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
        this.displayName = user.getDisplayName();
        this.status = user.getStatus();
        this.authorities = List.copyOf(authorities);
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean isSeller() {
        return authorities.stream()
                .anyMatch(authority -> "ROLE_SELLER".equals(authority.getAuthority()));
    }

    public AuthenticatedUserDto toAuthenticatedUserDto() {
        return new AuthenticatedUserDto(
                userId,
                email,
                status,
                displayName,
                isSeller()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}
