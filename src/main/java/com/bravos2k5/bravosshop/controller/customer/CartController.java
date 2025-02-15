package com.bravos2k5.bravosshop.controller.customer;

import com.bravos2k5.bravosshop.dto.cart.CartItemDto;
import com.bravos2k5.bravosshop.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
