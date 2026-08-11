package com.example.marketflow.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShowUserDto {
    private Long id;
    private String email;
    private UserStatus status;
    private String displayName;
}
