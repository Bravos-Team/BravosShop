package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.security.principal.TokenInfo;
import io.jsonwebtoken.Claims;

public interface JwtService {

    /**
     * @param id userId
     * @param expiration exp(ms)
     * @return token
     */
    String generateToken(TokenInfo tokenInfo, long expiration);

    String getUsername(String token);

    boolean validateToken(String token);

    boolean verifyUsername(String token, String username);

    long getExpiration(String token);

    Claims extractAllClaims(String token);

    String getUsername(Claims claims);

    String getRole(Claims claims);

    long getExpiration(Claims claims);

    TokenInfo getTokenInfoIfValid(String token);

}
