package com.bravos2k5.bravosshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/r/address")
public class AddressController {

    @GetMapping
    public String address() {
        return "address";
    }

}
