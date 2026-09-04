package com.example.marketflow.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.marketflow.AccountType;

class UserMapperTest {

    @Test
    void convertNormalizesEmailAndDisplayNameAndUsesPasswordHash() {
        RegisterRequest request = new RegisterRequest();
        request.setAccountType(AccountType.BUYER);
        request.setEmail(" Buyer@Example.com ");
        request.setPassword("plain-password");
        request.setDisplay_name(" Ivan ");

        UserEntity result = UserMapper.convert(request, "password-hash");

        assertEquals("buyer@example.com", result.getEmail());
        assertEquals("Ivan", result.getDisplayName());
        assertEquals("password-hash", result.getPasswordHash());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
    }
}
