package com.example.marketflow.Seller.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import com.example.marketflow.Seller.Service.SellerDashboardService;


@RequestMapping("/seller")
@Controller
@AllArgsConstructor
public class SellerDashboardController {
    private final SellerDashboardService service;

    @GetMapping("/account")
    public String showdashboard(HttpSession session,Model model){
        Long id =(Long)session.getAttribute("userId");
        if (id==null) return "redirect:/login";

        model.addAttribute("Info",service.showallproductBySellerID(id));

        return "seller/showDashboard";
        
    }
}
