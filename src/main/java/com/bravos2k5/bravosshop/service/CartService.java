package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.model.cart.Cart;

public interface CartService {

    Cart findCartByUserId(Long userId);

    Cart mergeCart(Long guestCartId, Long cartId);

    Cart updateCart(Cart cart);

    void deleteCart(Long cartId);

    Cart cleanCart(Long cartId);

}
