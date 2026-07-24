package com.example.marketflow.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static userenyt convert(Userdto dto) {
        return new userenyt(
                dto.getEmail().trim().toLowerCase(),
                dto.getPassword_hash(),
                dto.getDisplay_name().trim()
        );
    }
}
