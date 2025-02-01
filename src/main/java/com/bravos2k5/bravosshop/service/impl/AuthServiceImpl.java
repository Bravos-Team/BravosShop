package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.dto.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.AuthService;
import com.bravos2k5.bravosshop.service.CookieService;
import com.bravos2k5.bravosshop.service.JwtService;
import com.bravos2k5.bravosshop.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CookieService cookieService;

    @Autowired
    public AuthServiceImpl(JwtService jwtService, UserService userService,
                           BCryptPasswordEncoder passwordEncoder, CookieService cookieService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cookieService = cookieService;
    }

    @Override
    public void addTokenToCookie(String token, int exp) {
        Cookie cookie = new Cookie("token",token);
        cookie.setMaxAge(exp);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookieService.addCookie(cookie);
    }

    @Override
    public void removeToken() {
        cookieService.deleteCookie("token");
    }

    @Override
    public String getToken() {
        Cookie cookie = cookieService.getCookie("token");
        if(cookie == null) return "";
        return cookie.getValue();
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userService.findByUsername(username);
        if(user == null) return null;
        return passwordEncoder.matches(password,user.getPassword()) ? user : null;
    }

    @Override
    public String generateAccessToken(TokenInfo tokenInfo) {
        return jwtService.generateToken(tokenInfo,24 * 60 * 60 * 1000);
    }

}
