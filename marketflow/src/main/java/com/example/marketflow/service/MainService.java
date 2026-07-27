package com.example.marketflow.service;

import java.math.BigDecimal;
import java.util.List;

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

    @Transactional
    public void function1(Model model){
        model.addAttribute("products", rep.findAll());
    }

    @Transactional
    public String function2(Long id){
        return rep.findById(id)
                .orElseThrow()
                .getDescription();
    }

    @Transactional
    public void function3(Long buyerid, Long productid){
        repost.save(new cartitemsentity(buyerid, productid));
    }

    @Transactional
    public java.util.List<cartitemsentity> function4(){
        return repost.findAll();
    }

    @Transactional
    public Integer function5(Long id){
        return repost.findById(id)
                .orElseThrow()
                .getQuantity();
    }

    @Transactional
    public void function6(Long id,Boolean active){
        cartitemsentity item = repost.findById(id).orElseThrow();
        item.setSelected(active);
    }
    @Transactional
    public void function7(Long id){
        repost.deleteById(id);
    }
    @Transactional
    public BigDecimal function8(){
        List<Long> list=repost.findAll()
                .stream()
                .map(cartitemsentity::getProductid)
                .toList();
        List<productsEntity> list2=rep.findAllById(list);
        return list2.stream().map(productsEntity::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
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
