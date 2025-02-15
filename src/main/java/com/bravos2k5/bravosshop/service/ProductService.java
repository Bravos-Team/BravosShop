package com.bravos2k5.bravosshop.service;


import com.bravos2k5.bravosshop.dto.product.CreateProductDto;
import com.bravos2k5.bravosshop.dto.product.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.product.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.product.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);

    List<ProductDisplayDto> getProductsDisplay();

    List<ProductDisplayDto> getDiscountProducts();

    List<ProductDisplayDto> getTopSellerProducts();

    List<ProductDisplayDto> getNewestProducts();

    ProductDetailDto getProductDetailsById(Long id);

    void createProduct(CreateProductDto createProductDto);

    List<ProductDisplayDto> getRelatedProducts(Integer categoryId);

}
