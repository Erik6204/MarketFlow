package com.example.marketflow.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.marketflow.cart.cartitemsentity;
import com.example.marketflow.payment_cards.cartdto;
import com.example.marketflow.service.MainService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
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
    public String pos(Model model,@PathVariable("productId") Long productId,HttpSession session) {
        Long userId =
            (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        service.function3(userId, productId);

        return "redirect:/account/cart";
    }

    @GetMapping("/cart")
    public String car(Model model,HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<cartitemsentity> cartItems =service.function4(userId);

        model.addAttribute("cartItems", cartItems);

        return "Page4";
    }

    @PostMapping("/cart/items/{itemId}/quantity")
    public String first(Model model,@PathVariable("itemId")Long itemId,
    @RequestParam("quantity") Integer quantity,HttpSession session){
        
        Long userId =(Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("count", service.function5(itemId, userId, quantity));

        return "Page5";
    }

    @PostMapping("/cart/items/{itemId}/select")
    public String second(@PathVariable("itemId") Long itemId,@RequestParam("choise") Boolean choice,
    HttpSession session) 
    {
        Long userId =(Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }
        service.function6(itemId, userId, choice);
        return "Page6";
    }

    @PostMapping("/cart/items/{itemId}/delete")
    public String third(@PathVariable("itemId") Long itemId,HttpSession session){
        service.function7(itemId,(Long) session.getAttribute("userId"));
        return "Page7";
    }

    @GetMapping("/checkout")
    public String fourth(HttpSession session,Model model){
        Long userId =
            (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        BigDecimal total =
                service.function8(userId);

        model.addAttribute("sum", total);

        return "Page8";
    }

    @GetMapping("/account/cards")
    public String fifth(Model model,HttpSession session){
        model.addAttribute("cards", service.function9((Long)session.getAttribute("userId")));
        return "Page9";
    }

    @GetMapping("/cards/add")
    public String six(){
        return "Page10";
    }

    @PostMapping("/account/cards")
    public String seven(@ModelAttribute cartdto dto,HttpSession session){
        service.function10((Long)session.getAttribute("userId"),dto);
        return "Page11";
    }


}
