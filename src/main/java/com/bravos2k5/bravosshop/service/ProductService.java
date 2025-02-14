package com.bravos2k5.bravosshop.service;


import com.bravos2k5.bravosshop.dto.product.CreateProductDto;
import com.bravos2k5.bravosshop.dto.product.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.product.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);

    List<ProductDisplayDto> getProductsDisplay();

    List<ProductDisplayDto> getDiscountProducts();

    List<ProductDisplayDto> getTopSellerProducts();

    List<ProductDisplayDto> getNewestProducts();

    void createProduct(CreateProductDto createProductDto);

}
