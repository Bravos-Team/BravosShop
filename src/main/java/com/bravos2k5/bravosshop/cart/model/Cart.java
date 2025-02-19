package com.bravos2k5.bravosshop.cart.model;

import com.bravos2k5.bravosshop.common.utils.SnowFlakeId;
import com.bravos2k5.bravosshop.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements SnowFlakeId {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();

    public Cart(long id, User user) {
        this.id = id;
        this.user = user;
    }

}
