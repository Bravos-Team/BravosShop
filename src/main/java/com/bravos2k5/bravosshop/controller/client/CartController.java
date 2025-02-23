package com.bravos2k5.bravosshop.controller.client;

import com.bravos2k5.bravosshop.dto.AddToCartDto;
import com.bravos2k5.bravosshop.dto.CartItemDto;
import com.bravos2k5.bravosshop.dto.CartQuantityDto;
import com.bravos2k5.bravosshop.service.interfaces.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CartQuantityDto> modifyQuantity(@RequestBody CartQuantityDto cartQuantityDto) {
        try {
            CartQuantityDto afterCart = cartService.updateQuantity(cartQuantityDto);
            if(afterCart == null) {
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.ofNullable(cartService.updateQuantity(cartQuantityDto));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
