package com.bravos2k5.bravosshop.cart.service.interfaces;

import com.bravos2k5.bravosshop.cart.dto.AddToCartDto;
import com.bravos2k5.bravosshop.cart.dto.CartItemDto;
import com.bravos2k5.bravosshop.cart.model.Cart;
import com.bravos2k5.bravosshop.cart.model.CartItem;

import java.util.List;

public interface CartService {

    Cart findCartById(Long cartId);

    Cart findCartByUsername(String username);

    Cart findCartByUserId(Long userId);

    void mergeCart(Long guestCartId, Long cartId);

    Cart createGuestCart();

    void deleteCart(Long cartId);

    Cart cleanCart(Long cartId);

    CartItem updateQuantity(Long itemId, Long quantity);

    void addToCart(AddToCartDto addToCartDto);

    List<CartItemDto> findAllCartItemByCartId(Long cartId);

    List<CartItemDto> getCartItemsInSession();

    Cart getCartInSession();

}
