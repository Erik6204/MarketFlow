package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.service.AuthService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AccountController {
    private final AuthService authservice;
    @GetMapping("/Buyer/account")
    public String showAccount(HttpSession session,Model model){
        Long d=(Long)session.getAttribute("userId");
        if(d==null) return "redirect:/login";
        ShowUserDto ess=authservice.getUserById(d);

        model.addAttribute("email",ess.getEmail());
        model.addAttribute("displayName",ess.getDisplayName());
        model.addAttribute("status",ess.getStatus());
        return "showAccount";
    }

    
}
