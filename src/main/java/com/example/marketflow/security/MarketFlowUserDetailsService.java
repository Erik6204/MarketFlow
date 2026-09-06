package com.example.marketflow.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.Repository.userRoleRepository;
import com.example.marketflow.User.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarketFlowUserDetailsService implements UserDetailsService {
//загружает пользователя из базы данных для Spring Security во время входа.
    private final UserRepository userRepository;
    private final userRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        String normalizedEmail = username.toLowerCase().trim();
        UserEntity user = userRepository
                .findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        List<SimpleGrantedAuthority> authorities = userRoleRepository
                .findRoleNamesByUserId(user.getId())
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return new MarketFlowPrincipal(user, authorities);
    }
}
