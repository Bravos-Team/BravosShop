package com.bravos2k5.bravosshop.service.impl;


import com.bravos2k5.bravosshop.config.security.TokenInfo;
import com.bravos2k5.bravosshop.service.JwtService;
import com.bravos2k5.bravosshop.utils.KeyLoader;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private static final PublicKey publicKey = KeyLoader.loadPublicKey(System.getenv("PUBLIC_KEY_PATH"));
    private static final PrivateKey privateKey = KeyLoader.loadPrivateKey(System.getenv("PRIVATE_KEY_PATH"),
            System.getenv("PRIVATE_KEY_PASSWORD"));

    @Override
    public String generateToken(TokenInfo tokenInfo, long expiration) {
        return Jwts.builder()
                .subject(tokenInfo.getUsername())
                .claim("role",tokenInfo.getRole())
                .claim("name",tokenInfo.getName())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .issuedAt(new Date())
                .signWith(privateKey)
                .compact();
    }

    @Override
    public String getUsername(String token) {
       return extractAllClaims(token).get("sub", String.class);
    }

    @Override
    public boolean validateToken(String token) {
        return extractAllClaims(token) != null;
    }

    @Override
    public boolean verifyUsername(String token, String username) {
        Claims claims = extractAllClaims(token);
        return getUsername(claims).equals(username) && validateToken(token);
    }

    @Override
    public long getExpiration(String token) {
        return extractAllClaims(token).get("exp", Long.class);
    }

    @Override
    public Claims extractAllClaims(String token) {
        if(token == null || token.isBlank()) return null;
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("JWT Token is invalid");
            return null;
        }
    }

    @Override
    public String getUsername(Claims claims) {
        return claims.get("sub", String.class);
    }

    @Override
    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    @Override
    public long getExpiration(Claims claims) {
        return claims.get("exp", Long.class);
    }

    @Override
    public TokenInfo getTokenInfo(String token) {
        Claims claims = extractAllClaims(token);
        if(claims != null) {
            List<Map> roles = claims.get("role", List.class);
            String role = (String) roles.stream().toList().getFirst().get("authority");
            return new TokenInfo(claims.getSubject(),claims.get("name", String.class), List.of(new SimpleGrantedAuthority(role)));
        }
        return null;
    }

}
