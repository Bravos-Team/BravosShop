package com.bravos2k5.asm5.model.product;

import com.bravos2k5.asm5.enums.ProductStatus;
import com.bravos2k5.asm5.model.SnowFlakeId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements SnowFlakeId {

    @Id
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(127)")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(16)")
    private String unit;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String thumbnail;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String images = "{}";

    @Column(nullable = false)
    private Integer category;

    @Column(nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.IN_STOCK;

    public Double setUnitPrice(Double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }
        return this.unitPrice = unitPrice;
    }

}
