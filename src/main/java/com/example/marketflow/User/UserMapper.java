package com.example.marketflow.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserEntity convert(RegisterRequest dto, String passwordHash) {
        return new UserEntity(
                dto.getEmail().trim().toLowerCase(),
                passwordHash,
                dto.getDisplay_name().trim()
        );
    }

    public static ShowUserDto toShowUserDto(UserEntity entity) {
        return new ShowUserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getStatus(),
                entity.getDisplayName()
        );
    }
}
