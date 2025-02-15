package com.bravos2k5.bravosshop.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/p/forget")
public class ForgetController {

    @GetMapping("/{x}")
    public String forget(@PathVariable int x) {
        return "forget/step" + x;
    }

}
