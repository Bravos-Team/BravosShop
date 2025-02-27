package com.bravos2k5.bravosshop.controller.client;

import com.bravos2k5.bravosshop.dto.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.ProductDisplayDto;
import com.bravos2k5.bravosshop.dto.ProductRating;
import com.bravos2k5.bravosshop.service.interfaces.ProductService;
import com.bravos2k5.bravosshop.service.interfaces.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/p/product")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
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

}
