package com.bravos2k5.bravosshop.model.order;

import com.bravos2k5.bravosshop.enums.OrderStatus;
import com.bravos2k5.bravosshop.enums.PaymentStatus;
import com.bravos2k5.bravosshop.utils.SnowFlakeId;
import com.bravos2k5.bravosshop.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order implements SnowFlakeId {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Collection<OrderDetail> orderDetails;

    public Double total() {
        return orderDetails.stream().mapToDouble(OrderDetail::getFinalPrice).sum();
    }

}
