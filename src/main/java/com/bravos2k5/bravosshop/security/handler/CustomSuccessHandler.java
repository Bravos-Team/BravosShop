package com.bravos2k5.bravosshop.security.handler;

import com.bravos2k5.bravosshop.security.principal.TokenInfo;
import com.bravos2k5.bravosshop.model.user.User;
import com.bravos2k5.bravosshop.service.interfaces.AuthService;
import com.bravos2k5.bravosshop.service.interfaces.CartService;
import com.bravos2k5.bravosshop.service.interfaces.CookieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final AuthService authService;
    private final CookieService cookieService;
    private final CartService cartService;

    @Autowired
    public CustomSuccessHandler(AuthService authService, CookieService cookieService, CartService cartService) {
        this.authService = authService;
        this.cookieService = cookieService;
        this.cartService = cartService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token;
        String username;
        if(authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            username = oAuth2User.getAttribute("username");
            String displayName = oAuth2User.getAttribute("displayName");
            Collection<GrantedAuthority> role = new ArrayList<>(oAuth2User.getAuthorities());
            TokenInfo tokenInfo = new TokenInfo(username,displayName,role);
            token = authService.generateAccessToken(tokenInfo);
        }
        else if (authentication.getPrincipal() instanceof User user) {
            username = user.getUsername();
            TokenInfo tokenInfo = new TokenInfo(user.getUsername(),user.getDisplayName(), new ArrayList<>(user.getAuthorities()));
            token = authService.generateAccessToken(tokenInfo);
        }
        else {
            throw new IllegalArgumentException("Not support this method");
        }
        authService.addTokenToCookie(token,24 * 3600);

        this.mergeCartHandle(username);

        super.onAuthenticationSuccess(request,response,authentication);
    }

    private void mergeCartHandle(String username) {
        try {
            cartService.mergeCart(username);
        } catch (Exception e) {
            logger.error("Cannot merge cart: " + e.getMessage());
        } finally {
            cookieService.deleteCookie("guestCartId");
        }
    }

}
