package com.example.marketflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.payment_cards.cartdto;
import com.example.marketflow.service.MainService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/account/{id}")
public class Maincontroller {
    private final MainService service;
    public Maincontroller(MainService s){this.service=s;}

    @GetMapping("/catalog")
    public String show(Model model){
        service.function1(model);
        return "Page1";
    }

    @GetMapping("/products/{productId}")
    public String prod(Model model,@PathVariable("productId") Long id){
        model.addAttribute("product", service.function2(id));
        return "Page2";
    }

    @PostMapping("/cart/items/{productId}")
    public String pos(Model model,@PathVariable("productId") Long id,HttpSession session){
        service.function3((Long)session.getAttribute("userId"),id);
        return "Page3";
        
    }

    @GetMapping("/cart")
    public String car(Model model){
        model.addAllAttributes(service.function4());
        return "Page4";
    }

    @PostMapping("/cart/items/{itemId}/quantity")
    public String first(Model mode,@PathVariable("itemId")Long id){
        mode.addAttribute("count",service.function5(id));
        return "Page5";
    }

    @PostMapping("/cart/items/{itemId}/select")
    public String second(Model model,@PathVariable("itemId")Long id,@ModelAttribute Boolean choise){
        service.function6(id,choise);
        return "Page6";
    }

    @PostMapping("/cart/items/{itemId}/delete")
    public String third(@PathVariable("itemId") Long id){
        service.function7(id);
        return "Page7";
    }

    @GetMapping("/checkout")
    public String fourth(Model model){
        model.addAttribute("sum", service.function8());
        return "Page8";
    }

    @GetMapping("/account/cards")
    public String fifth(Model model,HttpSession session){
        model.addAttribute("cards", service.function9((Long)session.getAttribute("userId")));
        return "Page9";
    }

    @GetMapping("/account/cards/add")
    public String six(){
        return "Page10";
    }

    @PostMapping("/account/cards")
    public String seven(@ModelAttribute cartdto dto,HttpSession session){
        service.function10((Long)session.getAttribute("userId"),dto);
        return "Page11";
    }


}
