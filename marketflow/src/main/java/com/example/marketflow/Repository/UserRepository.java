package com.example.marketflow.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.marketflow.User.userenyt;

public interface UserRepository extends JpaRepository<userenyt, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<userenyt> findByEmailIgnoreCaseAndPasswordHash(String email, String passwordHash);
}
