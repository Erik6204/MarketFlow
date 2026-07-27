package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.marketflow.User.Userdto;
import com.example.marketflow.User.logindto;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class Authcontroller {

    private final AuthService authservice;

    public Authcontroller(AuthService authservice) {
        this.authservice = authservice;
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
    public String saver(@Valid @ModelAttribute Userdto userDto,BindingResult bindingResult) 
    {
        return authservice.funtction1(userDto,bindingResult);
    }

    @PostMapping("/login/success")
    public String loginsuc(@Valid @ModelAttribute logindto log,BindingResult bindingResult,Model model,HttpSession session)
    {
        return authservice.function2(log, bindingResult, model, session);
    }

    @GetMapping("/account")
    public String acc(HttpSession session,Model model){
        return authservice.function3(session, model);
    }

    
}
