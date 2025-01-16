package com.bravos2k5.asm5.model.user;

import com.bravos2k5.asm5.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    private String note;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private AddressType type = AddressType.HOME;

}
