package com.example.marketflow.User;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class logindto {
    
    @Nonnull
    String Email;

    @Nonnull
    String password_hash;
}
