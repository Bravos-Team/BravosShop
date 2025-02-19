package com.bravos2k5.bravosshop.review.model;

import com.bravos2k5.bravosshop.common.utils.SnowFlakeId;
import com.bravos2k5.bravosshop.user.model.User;
import com.bravos2k5.bravosshop.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class Review implements SnowFlakeId {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double stars;

    @Column(columnDefinition = "nvarchar(255)")
    private String comment;

    @Builder.Default
    private LocalDate reviewDate = LocalDate.now();

}
