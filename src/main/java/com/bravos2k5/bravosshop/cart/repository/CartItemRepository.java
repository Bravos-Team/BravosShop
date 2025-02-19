package com.bravos2k5.bravosshop.cart.repository;

import com.bravos2k5.bravosshop.cart.model.Cart;
import com.bravos2k5.bravosshop.cart.model.CartItem;
import com.bravos2k5.bravosshop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByProductAndCart(Product product, Cart cart);

}