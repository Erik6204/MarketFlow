package com.example.marketflow.User;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Userdto {

    @NotBlank(message = "id не должен быть пустым")
    private Long id;

    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "email должен быть ")
    private String email;

    @NotBlank(message = "пароль не должен быть пустым")
    private String password_hash;

    @NotBlank(message = "ФИО не должен быть пустым")
    private String display_name;

    @NotBlank(message = "status не должен быть пустым")
    private UserStatus status;

    @NotBlank(message = "не должен быть пустым")
    private Instant createdAt;

    @NotBlank(message = "не должен быть пустым")
    private Instant updatedAt;
}
