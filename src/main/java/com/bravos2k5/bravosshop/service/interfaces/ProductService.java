package com.bravos2k5.bravosshop.service.interfaces;


import com.bravos2k5.bravosshop.dto.CreateProductDto;
import com.bravos2k5.bravosshop.dto.ProductAdminDto;
import com.bravos2k5.bravosshop.dto.ProductDetailDto;
import com.bravos2k5.bravosshop.dto.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<ProductAdminDto> getProductAdminDisplay(int page, int pageSize);

    List<ProductDisplayDto> getProductsDisplayByCategory(Integer categoryId);

    List<ProductDisplayDto> getProductsDisplayByCategoryOnPromotion(Integer categoryId);

}
