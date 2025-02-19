package com.bravos2k5.bravosshop.cart.controller;

import com.bravos2k5.bravosshop.cart.dto.AddToCartDto;
import com.bravos2k5.bravosshop.cart.dto.CartItemDto;
import com.bravos2k5.bravosshop.cart.dto.CartQuantityDto;
import com.bravos2k5.bravosshop.cart.service.interfaces.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/p/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String cart(Model model) {
        List<CartItemDto> cartItemDtoList = cartService.getCartItemsInSession();
        model.addAttribute("cartItems",cartItemDtoList);
        return "cart";
    }

    @PostMapping("/addToCart")
    public String addToCategory(@ModelAttribute AddToCartDto addToCartDto) {
        try {
            cartService.addToCart(addToCartDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/p/cart";
    }

    @ResponseBody
    @PostMapping("/modifyCart")
    public String modifyQuantity(@RequestBody CartQuantityDto cartQuantityDto) {
        return "a";
    }

}
