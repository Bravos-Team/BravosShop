package com.bravos2k5.bravosshop.service.interfaces;

import com.bravos2k5.bravosshop.dto.RegisterDto;
import com.bravos2k5.bravosshop.security.principal.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;

public interface AuthService {

    void sendVerifyEmailUrl(RegisterDto registerDto);

    /**
     * Check token trên redis coi có ko, hợp lê lưu vào db, ko thì thôi
     * @param token token
     * @return username, exist = đã tồn tại, null = ko hợp lệ hoặc hết hạn, error = lỗi gì k biết nx
     */
    String authenticateVerifyToken(String token);

    void addTokenToCookie(String token, int exp);

    void removeToken();

    String getToken();

    User authenticateUser(String username, String password);

    String generateAccessToken(TokenInfo tokenInfo);

}
