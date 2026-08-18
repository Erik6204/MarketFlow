package com.example.marketflow.User;

import com.example.marketflow.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    
    @NotNull(message = "выберите тип аккаунта")
    private AccountType accountType;

    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "некорректный формат email")
    @Size(max = 320, message = "email не должен быть длиннее 320 символов")
    private String email;

    @NotBlank(message = "пароль не должен быть пустым")
    @Size(min = 8, max = 100, message = "пароль должен содержать от 8 до 100 символов")
    private String password;

    @NotBlank(message = "имя не должно быть пустым")
    @Size(max = 100, message = "имя не должно быть длиннее 100 символов")
    private String display_name;

    
    

}
