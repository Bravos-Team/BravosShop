package com.bravos2k5.bravosshop.controller.admin;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/a/address")
public class AddressAdminController {

    @GetMapping
    public String address() {
        return "admin/address";
    }

}
