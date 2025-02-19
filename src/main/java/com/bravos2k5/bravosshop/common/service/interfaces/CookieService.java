package com.bravos2k5.bravosshop.common.service.interfaces;

import jakarta.servlet.http.Cookie;

public interface CookieService {

    Cookie getCookie(String name);

    void addCookie(Cookie cookie);

    void deleteCookie(String name);

    String getValue(String name);

}
