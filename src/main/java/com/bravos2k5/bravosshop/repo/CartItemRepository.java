package com.bravos2k5.bravosshop.repo;

import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.cart.CartItem;
import com.bravos2k5.bravosshop.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByProductAndCart(Product product, Cart cart);

}