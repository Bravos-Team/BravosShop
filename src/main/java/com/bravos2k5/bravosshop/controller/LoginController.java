package com.bravos2k5.bravosshop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !authentication.isAuthenticated() ? "login" : "redirect:/home";
    }

}
