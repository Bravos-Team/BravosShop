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
        return "admin/user-detail";
    }

    @GetMapping("/user/address")
    public String address() {
        return "admin/address";
    }

    @GetMapping("/product")
    public String product() {
        return "admin/product";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "admin/product-detail";
    }

    @GetMapping("/category")
    public String category() {
        return "admin/category";
    }

    @GetMapping("/product/statistic")
    public String pStatistic() {
        return "admin/product-statistic";
    }

    @GetMapping("/general-statistic")
    public String gStatistic() {
        return "admin/general-statistic";
    }

    @GetMapping("/add-product")
    public String addProduct() {
        return "admin/add-product";
    }

}
