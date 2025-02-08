package com.bravos2k5.bravosshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/a/category")
public class CategoryAdminController {

    @GetMapping
    public String category(Model model) {
        return "/admin/category";
    }

}
