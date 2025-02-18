package com.bravos2k5.bravosshop.model.cart;

import com.bravos2k5.bravosshop.model.SnowFlakeId;
import com.bravos2k5.bravosshop.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id","product_id"}))
public class CartItem implements SnowFlakeId {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Builder.Default
    private Long quantity = 1L;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product, cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(product);
    }

}
