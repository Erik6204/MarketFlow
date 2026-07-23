package com.example.marketflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.User.UserMapper;
import com.example.marketflow.User.Userdto;
import com.example.marketflow.User.logindto;

import jakarta.validation.Valid;

@Controller
public class Authcontroller {

    private final UserRepository userRepository;

    @Autowired
    public Authcontroller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/")
    public String authPage() {
        return "auth-page";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register-page";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login-page";
    }

    @PostMapping("/register/succ")
    public String saver(@Valid @ModelAttribute Userdto userDto){
        if (!userRepository.existsByEmailIgnoreCase(userDto.getEmail())){
            userRepository.save(UserMapper.convert(userDto));
            return "true";
        }
        return "fal";
    }

    @PostMapping("/login/success")
    public String loginsuc(@Valid @ModelAttribute logindto log){
        if (userRepository.existsByEmailIgnoreCase(log.getEmail()) && userRepository.existsByPasswordhashIgnoreCase(log.getPassword_hash())){
            return "login/success";
        }
        return "login/unsuccess";
    }
}
