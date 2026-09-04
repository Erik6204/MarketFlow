package com.example.marketflow.MVCTHymeleafcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.marketflow.User.LoginRequest;
import com.example.marketflow.User.RegisterRequest;
import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authservice;

    public AuthController(AuthService authservice) {
        this.authservice = authservice;
    }

    @GetMapping("/")
    public String authPage() {
        return "authPage";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "registerPage";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "registerPage";
        }

        authservice.register(request);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest log,BindingResult bindingResult,HttpSession session)
    {
        if (bindingResult.hasErrors()) {
            return "loginPage";
        }
        ShowUserDto user=authservice.authenticate(log);
 
        session.setAttribute("userId", user.getId());
        return authservice.isSeller(user.getId())?"redirect:/seller/account" : "redirect:/Buyer/account";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    

    
}
