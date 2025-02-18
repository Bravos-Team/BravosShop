package com.bravos2k5.bravosshop.service.constract;

import com.bravos2k5.bravosshop.dto.cart.AddToCartDto;
import com.bravos2k5.bravosshop.dto.cart.CartItemDto;
import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.cart.CartItem;

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
