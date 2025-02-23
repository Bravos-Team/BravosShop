package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.dto.AddToCartDto;
import com.bravos2k5.bravosshop.dto.CartItemDto;
import com.bravos2k5.bravosshop.dto.CartQuantityDto;
import com.bravos2k5.bravosshop.model.cart.Cart;

import java.util.List;

public interface CartService {

    Cart findCartById(Long cartId);

    Cart findCartByUsername(String username);

    Cart findCartByUserId(Long userId);

    void mergeCart(Long guestCartId, Long cartId);

    Cart createGuestCart();

    void deleteCart(Long cartId);

    Cart cleanCart(Long cartId);

    CartQuantityDto updateQuantity(CartQuantityDto cartQuantityDto);

    void addToCart(AddToCartDto addToCartDto);

    List<CartItemDto> findAllCartItemByCartId(Long cartId);

    List<CartItemDto> getCartItemsInSession();

    Cart getCartInSession();

}
