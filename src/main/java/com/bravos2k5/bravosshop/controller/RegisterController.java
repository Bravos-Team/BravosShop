package com.bravos2k5.bravosshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/p/register")
public class RegisterController {

    @GetMapping
    public String register() {
        return "register";
    }

}
