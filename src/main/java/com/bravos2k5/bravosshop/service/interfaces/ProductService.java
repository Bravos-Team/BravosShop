package com.bravos2k5.bravosshop.service.interfaces;


import com.bravos2k5.bravosshop.dto.CreateProductDto;
import com.bravos2k5.bravosshop.dto.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.ProductDisplayDto;
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
