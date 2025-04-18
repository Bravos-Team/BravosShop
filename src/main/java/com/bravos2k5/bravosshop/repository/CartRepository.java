package com.bravos2k5.bravosshop.repository;

import com.bravos2k5.bravosshop.dto.CartItemDto;
import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    @Query("select new " +
            "com.bravos2k5.bravosshop.dto.CartItemDto(ci.id,ci.product.name,ci.product.category.name," +
            "ci.product.thumbnail,ci.quantity,ci.product.unitPrice,ci.product.discountValue,ci.product.promotionType) " +
            "from CartItem ci " +
            "where ci.cart.id = :cartId")
    List<CartItemDto> getAllCartItemId(@Param("cartId") Long cartId);

    Cart findByUser_Username(String userUsername);
}
