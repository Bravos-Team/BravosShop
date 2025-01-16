package com.bravos2k5.asm5.model.user;

import com.bravos2k5.asm5.enums.UserStatus;
import com.bravos2k5.asm5.model.SnowFlakeId;
import com.bravos2k5.asm5.model.cart.Cart;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails, SnowFlakeId {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 31)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "default_address_id")
    private Address defaultAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == UserStatus.ACTIVE || status == UserStatus.INACTIVE;
    }


}
