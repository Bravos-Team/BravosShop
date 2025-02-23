package com.bravos2k5.bravosshop.model.user;

import com.bravos2k5.bravosshop.enums.UserStatus;
import com.bravos2k5.bravosshop.utils.SnowFlakeId;
import com.bravos2k5.bravosshop.model.cart.Cart;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements Serializable, UserDetails, SnowFlakeId {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 31)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 63, columnDefinition = "nvarchar(63)")
    private String displayName;

    @Column(nullable = false, unique = true, length = 127)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @Column(length = 63, unique = true)
    private String googleId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean admin = false;

    @ManyToOne
    @JoinColumn(name = "default_address_id")
    private Address defaultAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(admin ? "ROLE_ADMIN" : "ROLE_USER"));
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

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
