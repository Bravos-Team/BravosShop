package com.bravos2k5.bravosshop.product.service.interfaces;


import com.bravos2k5.bravosshop.product.dto.CreateProductDto;
import com.bravos2k5.bravosshop.product.dto.ProductDetailDto;
import com.bravos2k5.bravosshop.product.dto.ProductDisplayDto;
import com.bravos2k5.bravosshop.product.model.Product;

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
