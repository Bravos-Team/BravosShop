package com.bravos2k5.bravosshop.model.product;

import com.bravos2k5.bravosshop.enums.ProductStatus;
import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.model.SnowFlakeId;
import com.bravos2k5.bravosshop.model.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private ProductStatus status = ProductStatus.IN_STOCK;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private PromotionType promotionType = PromotionType.NO_PROMOTION;

    @Builder.Default
    @Column(nullable = false)
    private Double discountValue = 0d;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Double setUnitPrice(Double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }
        return this.unitPrice = unitPrice;
    }

}
