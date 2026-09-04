package com.example.marketflow.User;

public record AuthenticatedUserDto(
        Long id,
        String email,
        UserStatus status,
        String displayName,
        boolean seller
) {
    public static AuthenticatedUserDto from(ShowUserDto user, boolean seller) {
        return new AuthenticatedUserDto(
                user.getId(),
                user.getEmail(),
                user.getStatus(),
                user.getDisplayName(),
                seller
        );
    }
}
