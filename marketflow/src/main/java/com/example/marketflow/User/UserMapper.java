package com.example.marketflow.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static Userdto conver(userenyt s){
        return new Userdto(s.getId(),s.getEmail(),s.getPassword_hash(),s.getDisplay_name(),s.getStatus(),s.getCreatedAt(),s.getUpdatedAt());

    }
    public static userenyt convert(Userdto dto){
        return new userenyt(dto.getId(),dto.getEmail(),dto.getPassword_hash(),dto.getDisplay_name(),dto.getStatus(),dto.getCreatedAt(),dto.getUpdatedAt());
    }
}
