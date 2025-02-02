package com.bravos2k5.bravosshop.model.invoice;

import com.bravos2k5.bravosshop.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetail implements Serializable {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long quantity;

    @Builder.Default
    private Double discount = 0d;

    public Double getFinalPrice() {
        return (price - discount) * quantity;
    }

    public Double getTotalPrice() {
        return price * quantity;
    }

    public void setQuantity(Long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than or equal to 0");
        }
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        if (price < 0.0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0");
        }
        this.price = price;
    }

}
