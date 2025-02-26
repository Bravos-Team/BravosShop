package com.bravos2k5.bravosshop.controller.admin;

import com.bravos2k5.bravosshop.dto.CreateProductDto;
import com.bravos2k5.bravosshop.dto.ProductAdminDto;
import com.bravos2k5.bravosshop.model.product.Product;
import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/a/product")
public class ProductAdminController {

    private final ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProduct(@RequestParam(defaultValue = "1") int page, Model model) {
        final int pageSize = 20;
        Page<ProductAdminDto> products = productService.getProductAdminDisplay(page,pageSize);
        model.addAttribute("products",products);
        model.addAttribute("currentPage",page);
        return "admin/product";
    }

    @GetMapping("/detail/{id}")
    public String showDetailProduct(@PathVariable long id, Model model) {
        Product product = productService.findById(id);
        if(product == null) {
            return "redirect:/a/product";
        }
        model.addAttribute("product",product);
        return "admin/product-detail";
    }

    @GetMapping("/add")
    public String addProduct() {
        return "admin/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute CreateProductDto createProductDto, RedirectAttributes redirectAttributes) {
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
