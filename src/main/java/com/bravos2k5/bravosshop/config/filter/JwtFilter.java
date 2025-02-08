package com.bravos2k5.bravosshop.config.filter;

import com.bravos2k5.bravosshop.config.security.JwtAuthenticationToken;
import com.bravos2k5.bravosshop.config.security.TokenInfo;
import com.bravos2k5.bravosshop.service.AuthService;
import com.bravos2k5.bravosshop.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtService jwtService;

    public JwtFilter(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        if(uri.startsWith("/res") || uri.startsWith("/error")) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authService.getToken();
        if(token != null) {
            TokenInfo tokenInfo = jwtService.getTokenInfo(token);
            if (tokenInfo == null) {
                authService.removeToken();
                filterChain.doFilter(request,response);
                return;
            }
            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(token,tokenInfo);
            authenticated.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }
        filterChain.doFilter(request,response);

    }

}
