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

    private Double price;

    private Long quantity;

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
