package com.bravos2k5.bravosshop.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.security.Principal;

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


}
