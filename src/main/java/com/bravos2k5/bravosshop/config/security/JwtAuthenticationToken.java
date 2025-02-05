package com.bravos2k5.bravosshop.config.security;

import com.bravos2k5.bravosshop.dto.TokenInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String jwt;
    private final Principal principal;

    public JwtAuthenticationToken(String jwt, Principal principal) {
        super(null);
        this.jwt = jwt;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        TokenInfo tokenInfo = (TokenInfo) getPrincipal();
        return tokenInfo.getRole();
    }
}
