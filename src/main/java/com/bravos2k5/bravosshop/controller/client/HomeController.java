package com.bravos2k5.bravosshop.controller.client;

import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("discountProducts",productService.getDiscountProducts());
        model.addAttribute("topsellerProducts",productService.getTopSellerProducts());
        model.addAttribute("newestProducts",productService.getNewestProducts());
        return "home";
    }

}
