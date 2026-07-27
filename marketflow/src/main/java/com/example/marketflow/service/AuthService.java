package com.example.marketflow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.User.UserMapper;
import com.example.marketflow.User.Userdto;
import com.example.marketflow.User.logindto;
import com.example.marketflow.User.userenyt;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String funtction1 (Userdto userDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "register-page";
        }

        if (userRepository.existsByEmailIgnoreCase(userDto.getEmail())) {
            return "page2";
        }

        userRepository.save(UserMapper.convert(userDto));
        return "page1";
    }
    @Transactional
    public String function2(@Valid @ModelAttribute logindto log,BindingResult bindingResult,Model model,HttpSession session
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
        session.setAttribute("userId", user.get().getId());
        model.addAttribute("email", user.get().getEmail());
        model.addAttribute("displayName", user.get().getDisplayName());
        model.addAttribute("status", user.get().getStatus());
        return "login/success";
    }
    @Transactional
    public String function3(HttpSession session,Model model){
        Long d=(Long)session.getAttribute("userId");
        if(d==null) return "redirect:/login";
        userenyt es=userRepository.findById(d).orElse(null);
        if (es == null) {
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("email",es.getEmail());
        model.addAttribute("displayname",es.getDisplayName());
        model.addAttribute("status",es.getStatus());
        return "login/success";
    }

    
    
}
