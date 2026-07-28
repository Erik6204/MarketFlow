package com.example.marketflow.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.marketflow.Repository.cartitemsRepository;
import com.example.marketflow.Repository.paymentcardsRepository;
import com.example.marketflow.Repository.productsRepository;
import com.example.marketflow.cart.cartitemsentity;
import com.example.marketflow.payment_cards.cartdto;
import com.example.marketflow.payment_cards.paymentcardsEntity;
import com.example.marketflow.products.productsEntity;
@Service
public class MainService {
    private final productsRepository rep;
    private final cartitemsRepository repost;
    private final paymentcardsRepository repository;
    public MainService(productsRepository rep,cartitemsRepository repost,paymentcardsRepository repository){
        this.rep=rep;
        this.repost=repost;
        this.repository=repository;
    }
    private cartitemsentity findOwnedCartItem(Long itemId,Long buyerId) 
    {
        return repost.findByIdAndBuyerid(itemId, buyerId).orElseThrow(
            () ->new IllegalArgumentException("Позиция корзины не найдена"));
    }

    @Transactional
    public void function1(Model model){
        model.addAttribute("products", rep.findAllByActiveTrueAndQuantityGreaterThan(0));
    }

    @Transactional
    public productsEntity function2(Long id){
        return rep.findById(id).orElseThrow();
    }

    @Transactional
    public void function3(Long buyerid,Long productid) {
        productsEntity product = rep.findById(productid).orElseThrow(() ->
        new IllegalArgumentException("Товар не найден"));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Товар больше не продаётся");
        }

        Optional<cartitemsentity> existingItem = repost.findByBuyeridAndProductid(buyerid,productid);

        int newQuantity = existingItem.map(item -> item.getQuantity() + 1).orElse(1);

        if (newQuantity > product.getQuantity()) {
            throw new IllegalArgumentException(
                    "Недостаточно товара на складе"
            );
        }

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(newQuantity);
        } else {
            repost.save(new cartitemsentity(buyerid,productid));
        }
    }

    @Transactional
    public List<cartitemsentity> function4(Long buyerid){
        return repost.findAllByBuyerid(buyerid);
    }

    @Transactional
    public Integer function5(Long id,Long buyerId,Integer quantity){
        cartitemsentity item = findOwnedCartItem(id, buyerId);
        productsEntity product = rep.findById(item.getProductid()).orElseThrow();

        int newQuantity = Math.max(1, Math.min(quantity, product.getQuantity()));
        item.setQuantity(newQuantity);
        return newQuantity;
    }

    @Transactional
    public void function6(Long id,Long buyerId,Boolean active){
        cartitemsentity item = repost.findByIdAndBuyerid(id,buyerId).orElseThrow();
        item.setSelected(active);
    }
    @Transactional
    public void function7(Long itemId,Long buyerId){
        repost.delete(findOwnedCartItem(itemId, buyerId));
    }
    @Transactional(readOnly = true)
    public BigDecimal function8(Long buyerId) {
        List<cartitemsentity> cartItems =repost.findAllByBuyeridAndSelectedTrue(buyerId);

        if (cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<Long> productIds = cartItems.stream().map(cartitemsentity::getProductid).toList();

        Map<Long, productsEntity> productsById =rep.findAllById(productIds).stream().collect(
                                Collectors.toMap(productsEntity::getId,product -> product));

        BigDecimal total = BigDecimal.ZERO;

        for (cartitemsentity item : cartItems) {
            productsEntity product =productsById.get(item.getProductid());

            if (product == null) {
                throw new IllegalArgumentException("Товар не найден: "+ item.getProductid());
            }

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new IllegalArgumentException("Товар больше не продаётся: "+ product.getId());
            }

            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Недостаточно товара: "+ product.getId());
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }
    @Transactional
    public List<paymentcardsEntity> function9(Long id){
        return repository.findAllByUserid(id);
    }

    @Transactional
    public void function10(Long id,cartdto dto){
        repository.save(new paymentcardsEntity(id,dto.getCardtoken(),dto.getMaskedNumber(),dto.getBalance()));
    }

}
