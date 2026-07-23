package com.example.marketflow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketflow.User.userenyt;

@Repository
public interface UserRepository extends JpaRepository<userenyt,Long>{
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPasswordhashIgnoreCase(String password_hash) ;   
}
