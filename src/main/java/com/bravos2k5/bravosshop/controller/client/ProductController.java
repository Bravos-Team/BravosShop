package com.bravos2k5.bravosshop.controller.client;

import com.bravos2k5.bravosshop.dto.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.ProductDisplayDto;
import com.bravos2k5.bravosshop.dto.ProductRating;
import com.bravos2k5.bravosshop.model.category.Category;
import com.bravos2k5.bravosshop.service.interfaces.CategoryService;
import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import com.bravos2k5.bravosshop.service.interfaces.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/p/product")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, ReviewService reviewService, CategoryService categoryService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProductDetailDto product = productService.getProductDetailsById(id);

        if (product == null) {
            return "error";
        }

        ProductRating productRating = reviewService.getProductRating(id);
        List<ProductDisplayDto> relatedProducts = productService.getRelatedProducts(product.getCatagoryId());

        model.addAttribute("product",product);
        model.addAttribute("productRating",productRating);
        model.addAttribute("relatedProducts",relatedProducts);

        return "detail";
    }

    @GetMapping("/category/{slug}")
    public String categoryProducts(@PathVariable String slug, Model model) {
        Category category = categoryService.findBySlug(slug);

        if(category == null) {
            log.error("Category not found");
            return "redirect:/";
        }

        Integer categoryId = category.getId();

        List<ProductDisplayDto> displayProductsOnPromotion = productService.getProductsDisplayByCategoryOnPromotion(categoryId);
        List<ProductDisplayDto> displayProducts = productService.getProductsDisplayByCategory(categoryId);

        model.addAttribute("discountProducts",displayProductsOnPromotion);
        model.addAttribute("products",displayProducts);
        model.addAttribute("a",category.getName() + " - Khuyễn mãi đặc biệt");
        model.addAttribute("b",category.getName());

        return "product";
    }



}
