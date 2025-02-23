package com.bravos2k5.bravosshop.security.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class TokenInfo implements Serializable, Principal {

    String username;
    String name;
    Collection<GrantedAuthority> role;

}
