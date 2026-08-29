
package com.example.marketflow.Seller.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.marketflow.Seller.Service.SellerProductService;
import com.example.marketflow.products.CreateProductRequest;
import com.example.marketflow.products.ProductMapper;
import com.example.marketflow.products.RestockProductRequest;
import com.example.marketflow.products.UpdateProductRequest;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RequestMapping("/seller")
@Controller
@AllArgsConstructor
public class SellerProductController {
    private final SellerProductService service;

    @GetMapping("/products/new")
    public String showCreateProduct(HttpSession session, Model model) {
        Long sellerId = (Long) session.getAttribute("userId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        model.addAttribute("product", new CreateProductRequest("", "", null, 0, ""));
        return "seller/createProduct";
    }

    @GetMapping("/products/{productId}")
    public String showProduct(@PathVariable Long productId,HttpSession session,Model model){
        Long sellerId = (Long) session.getAttribute("userId");

        if (sellerId == null) {
            return "redirect:/login";
        }
        model.addAttribute("product",service.getProductById(productId,sellerId));
        model.addAttribute("restock", new RestockProductRequest(null));
        return "seller/showProduct";
    }

    @PostMapping("/products")
    public String createProduct(
            @Valid @ModelAttribute("product") CreateProductRequest request,
            BindingResult bindingResult,
            HttpSession session
    ){
        Long sellerId = (Long) session.getAttribute("userId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "seller/createProduct";
        }

        Long productId = service.createProduct(request, sellerId);

        return "redirect:/seller/products/" + productId;
    }

    @GetMapping("/products/{productId}/edit")
    public String ShowProductEdit(@PathVariable Long productId,HttpSession session,Model model){

        Long SellerId=(Long)session.getAttribute("userId");
        if (SellerId==null)return "redirect:/login";

        model.addAttribute("product",ProductMapper.toUpdate(service.getProductById(productId, SellerId)));
        model.addAttribute("productId", productId);
        return "seller/editProduct";

    }
    
    @PostMapping("/products/{productId}/edit")
    public String EditProductData(@PathVariable Long productId,@Valid @ModelAttribute("product") UpdateProductRequest dto,BindingResult bindingResult,HttpSession session,Model model)
    {
        Long sellerId=(Long) session.getAttribute("userId");
        if (sellerId==null)return "redirect:/login";
        if (bindingResult.hasErrors()){
            model.addAttribute("productId",productId);
            return "seller/editProduct";
        }
        service.updateProduct(productId,sellerId,dto);
        
        return "redirect:/seller/products/" + productId;


    }

    @PostMapping("/products/{productId}/disable")
    public String DisableProduct(@PathVariable Long productId,HttpSession session){
        
        Long SellerId=(Long) session.getAttribute("userId");

        if (SellerId==null)return "redirect:/login";

        service.DisableProduct(productId, SellerId);

        return "redirect:/seller/products/" + productId;
    }

    @PostMapping("/products/{productId}/enable")
    public String EnableProduct(@PathVariable Long productId,HttpSession session){
        Long SellerId=(Long) session.getAttribute("userId");

        if (SellerId==null)return "redirect:/login";

        service.EnableProduct(productId, SellerId);

        return "redirect:/seller/products/" + productId;
    }

    @PostMapping("/products/{productId}/restock")
    public String RestokeQuanityProduct(@PathVariable Long productId,
        @Valid @ModelAttribute("restock") RestockProductRequest dto,BindingResult Result,HttpSession session,Model model){
        Long SellerId=(Long) session.getAttribute("userId");
        if (SellerId==null)return "redirect:/login";

        if (Result.hasErrors()) {
            model.addAttribute(
                    "product",
                    service.getProductById(productId, SellerId)
            );

            return "seller/showProduct";
        }

        service.RestokeQuanityProduct(productId, SellerId, dto.amount());

        return "redirect:/seller/products/" + productId;
    }

}
