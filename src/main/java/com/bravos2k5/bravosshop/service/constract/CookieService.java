package com.bravos2k5.bravosshop.service.constract;

import jakarta.servlet.http.Cookie;

public interface CookieService {

    Cookie getCookie(String name);

    void addCookie(Cookie cookie);

    void deleteCookie(String name);

    String getValue(String name);

}
