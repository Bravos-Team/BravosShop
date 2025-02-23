package com.bravos2k5.bravosshop.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        log.error(exception.getMessage());
        int errCode = -1;
        if(exception.getMessage().equalsIgnoreCase("User account is not enabled")) {
            errCode = 1;
        }
        else if (exception.getMessage().equalsIgnoreCase("User account is banned")) {
            errCode = 2;
        }
        response.sendRedirect("/login?error=" + errCode);
    }

}
