package com.bravos2k5.bravosshop.model.user;

import com.bravos2k5.bravosshop.enums.AddressType;
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

    @Column(nullable = false, columnDefinition = "nvarchar(63)")
    private String street;

    @Column(nullable = false, columnDefinition = "nvarchar(31)")
    private String ward;

    @Column(nullable = false, columnDefinition = "nvarchar(31)")
    private String district;

    @Column(nullable = false, columnDefinition = "nvarchar(31)")
    private String city;

    @Column(columnDefinition = "nvarchar(255)")
    private String note;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private AddressType type = AddressType.HOME;

}
