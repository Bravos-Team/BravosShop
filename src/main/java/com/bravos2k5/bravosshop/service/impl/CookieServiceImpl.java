package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {

    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @Autowired
    public CookieServiceImpl(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    @Override
    public Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    @Override
    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    @Override
    public void deleteCookie(String name) {
        Cookie cookie = new Cookie(name,"");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
