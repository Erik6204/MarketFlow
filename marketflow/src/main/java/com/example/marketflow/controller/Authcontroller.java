package com.example.marketflow.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.User.UserMapper;
import com.example.marketflow.User.Userdto;
import com.example.marketflow.User.logindto;
import com.example.marketflow.User.userenyt;

import jakarta.validation.Valid;

@Controller
public class Authcontroller {

    private final UserRepository userRepository;

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
    public String saver(
            @Valid @ModelAttribute Userdto userDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register-page";
        }

        if (userRepository.existsByEmailIgnoreCase(userDto.getEmail())) {
            return "page2";
        }

        userRepository.save(UserMapper.convert(userDto));
        return "page1";
    }

    @PostMapping("/login/success")
    public String loginsuc(
            @Valid @ModelAttribute logindto log,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "login-page";
        }

        Optional<userenyt> user = userRepository.findByEmailIgnoreCaseAndPasswordHash(
                log.getEmail(),
                log.getPassword_hash()
        );

        if (user.isEmpty()) {
            return "login/unsuccess";
        }

        model.addAttribute("email", user.get().getEmail());
        model.addAttribute("displayName", user.get().getDisplayName());
        model.addAttribute("status", user.get().getStatus());
        return "login/success";
    }
}
