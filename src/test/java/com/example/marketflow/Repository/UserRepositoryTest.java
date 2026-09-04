package com.example.marketflow.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.marketflow.User.UserEntity;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindEmailIgnoringCase() {
        userRepository.saveAndFlush(
                new UserEntity("Buyer@Example.com", "password-hash", "Ivan")
        );

        assertTrue(userRepository.existsByEmailIgnoreCase("BUYER@example.COM"));
        UserEntity found = userRepository
                .findByEmailIgnoreCase("buyer@example.com")
                .orElseThrow();
        assertEquals("Ivan", found.getDisplayName());
    }
}
