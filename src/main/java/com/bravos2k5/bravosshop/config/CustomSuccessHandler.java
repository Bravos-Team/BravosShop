package com.bravos2k5.bravosshop.config;

import com.bravos2k5.bravosshop.dto.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.AuthService;
import com.bravos2k5.bravosshop.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final CookieService cookieService;

    public CustomSuccessHandler(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String token;
        if(authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String username = oAuth2User.getAttribute("username");
            String displayName = oAuth2User.getAttribute("displayName");
            Collection<GrantedAuthority> role = oAuth2User.getAttribute("role");
            TokenInfo tokenInfo = new TokenInfo(username,displayName,role);
            token = authService.generateAccessToken(tokenInfo);
        }
        else if (authentication.getPrincipal() instanceof User user) {
            TokenInfo tokenInfo = new TokenInfo(user.getUsername(),user.getDisplayName(), user.getAuthorities());
            token = authService.generateAccessToken(tokenInfo);
        }
        else {
            throw new IllegalArgumentException("Not support this method");
        }
        authService.addTokenToCookie(token,24 * 3600);
        Cookie pageBeforeCookie = cookieService.getCookie("pageBeforeLogin");
        String previousPage = null;
        if (pageBeforeCookie != null) {
            previousPage = pageBeforeCookie.getValue();
        }
        response.sendRedirect(previousPage == null ? "/home" : previousPage);
    }

}
