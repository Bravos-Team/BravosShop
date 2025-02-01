package com.bravos2k5.bravosshop.service;

import com.bravos2k5.bravosshop.dto.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    void addTokenToCookie(String token, int exp);

    void removeToken();

    String getToken();

    User authenticateUser(String username, String password);

    String generateAccessToken(TokenInfo tokenInfo);

}
