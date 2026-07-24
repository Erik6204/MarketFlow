package com.example.marketflow.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class logindto {

    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "некорректный формат email")
    private String email;

    @NotBlank(message = "пароль не должен быть пустым")
    private String password_hash;
}
