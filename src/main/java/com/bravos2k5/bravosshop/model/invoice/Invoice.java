package com.bravos2k5.bravosshop.model.invoice;

import com.bravos2k5.bravosshop.enums.PaymentStatus;
import com.bravos2k5.bravosshop.model.SnowFlakeId;
import com.bravos2k5.bravosshop.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice implements SnowFlakeId {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PaymentStatus status;

}
