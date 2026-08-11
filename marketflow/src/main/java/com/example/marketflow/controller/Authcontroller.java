package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register-page";
        }

        authservice.register(request);

        return "redirect:/login";
    }

    @PostMapping("/login/success")
    public String login(@Valid @ModelAttribute LoginRequest log,BindingResult bindingResult,Model model,HttpSession session)
    {
        if (bindingResult.hasErrors()) {
            return "login-page";
        }
        java.util.Optional<ShowUserDto> user=authservice.authenticate(log);
        if (user.isEmpty()) {
            return "login/unsuccess";
        }
        session.setAttribute("userId", user.get().getId());
        model.addAttribute("email", user.get().getEmail());
        model.addAttribute("displayName", user.get().getDisplayName());
        model.addAttribute("status", user.get().getStatus());
        return "login/success";
    }

    @GetMapping("/account")
    public String showAccount(HttpSession session,Model model){
        Long d=(Long)session.getAttribute("userId");
        if(d==null) return "redirect:/login";
        ShowUserDto ess=authservice.getUserById(d);
        if (ess == null) {
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("email",ess.getEmail());
        model.addAttribute("displayName",ess.getDisplayName());
        model.addAttribute("status",ess.getStatus());
        return "login/success";
    }

    
}
