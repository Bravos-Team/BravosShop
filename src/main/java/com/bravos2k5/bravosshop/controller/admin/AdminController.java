package com.bravos2k5.bravosshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/p/admin")
public class AdminController {

    @GetMapping
    public String admin() {
        return "admin/admin-template";
    }

    @GetMapping("/user")
    public String user() {
        return "admin/user";
    }

    @GetMapping("/user/detail")
    public String detail() {
        return "admin/userdetail";
    }

    @GetMapping("/user/address")
    public String address() {
        return "admin/address";
    }

}
