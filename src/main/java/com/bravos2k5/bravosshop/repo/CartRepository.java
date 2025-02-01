package com.bravos2k5.bravosshop.repo;

import com.bravos2k5.bravosshop.model.cart.Cart;
import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}
