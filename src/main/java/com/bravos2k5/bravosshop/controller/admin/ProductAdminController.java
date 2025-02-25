package com.bravos2k5.bravosshop.controller.admin;

import com.bravos2k5.bravosshop.dto.CreateProductDto;
import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/a/product")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProduct(@RequestParam(defaultValue = "1") int page, Model model) {

        return "admin/product";
    }

    @GetMapping("/add")
    public String addProduct() {
        return "admin/add-product";
    }

    @PostMapping("/add")
    public String addProductPost(@ModelAttribute CreateProductDto createProductDto, RedirectAttributes redirectAttributes) {
        String message = "Success";
        try {
            productService.createProduct(createProductDto);
        } catch (Exception e) {
            message = e.getMessage();
        }
        redirectAttributes.addFlashAttribute("message",message);
        return "admin/add-product";
    }

}
