package com.bravos2k5.bravosshop.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/r/orders")
public class OrderController {

    @GetMapping
    public String order() {
        return "order";
    }

    @GetMapping("/details")
    public String detail() {
        return "order-detail";
    }

}
