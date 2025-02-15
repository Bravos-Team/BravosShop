package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.cart.CartItem;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.repo.CartRepository;
import com.bravos2k5.bravosshop.repo.UserRepository;
import com.bravos2k5.bravosshop.service.CartService;
import com.bravos2k5.bravosshop.utils.IdentifyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final IdentifyGenerator identifyGenerator;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, IdentifyGenerator identifyGenerator) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.identifyGenerator = identifyGenerator;
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) return null;
        return cartRepository.findByUser(user).orElse(null);
    }

    @Override
    public Cart mergeCart(Long guestCartId, Long cartId) {
        Cart guestCart = cartRepository.findById(guestCartId).orElse(null);
        Cart cart = cartRepository.findById(cartId).orElseThrow(IllegalArgumentException::new);

        if(guestCart == null) {
            return cart;
        }

        if(guestCart.getCartItems().isEmpty()) {
            cartRepository.deleteById(guestCartId);
            return cart;
        }

        guestCart.getCartItems().forEach(cartItem -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setId(identifyGenerator.generateId());
            newCartItem.setCart(cart);
            newCartItem.setProduct(cartItem.getProduct());
            newCartItem.setQuantity(cartItem.getQuantity());
            cart.getCartItems().remove(cartItem);
            cart.getCartItems().add(newCartItem);
        });

        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart updateCart(Cart cart) {
        return null;
    }

    @Override
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null || cart.getUser() != null) return;
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart cleanCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) return null;
        cart.getCartItems().clear();
        return cartRepository.saveAndFlush(cart);
    }

}
