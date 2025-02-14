package com.bravos2k5.bravosshop.repo;

import com.bravos2k5.bravosshop.dto.product.ProductDisplayDto;
import com.bravos2k5.bravosshop.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select new " +
            "com.bravos2k5.bravosshop.dto.product.ProductDisplayDto(p.id, p.name, p.thumbnail, p.category.name, p.unitPrice, p.promotionType, p.discountValue) " +
            "from Product p " +
            "WHERE p.promotionType != 0 and current timestamp between p.startTime and p.endTime " +
            "order by p.id desc " +
            "limit 15")
    List<ProductDisplayDto> getAllDisplayPromotionProduct();

    @Query("select new " +
            "com.bravos2k5.bravosshop.dto.product.ProductDisplayDto(p.id, p.name, p.thumbnail, p.category.name, p.unitPrice, p.promotionType, p.discountValue) " +
            "from Product p ")
    List<ProductDisplayDto> getAllDisplayProduct();

    @Query("select new " +
            "com.bravos2k5.bravosshop.dto.product.ProductDisplayDto(o.product.id, o.product.name, o.product.thumbnail, o.product.category.name," +
            " o.product.unitPrice, o.product.promotionType, o.product.discountValue) " +
            "from OrderDetail o " +
            "group by o.product.id, o.product.name, o.product.thumbnail, o.product.category.name, o.product.unitPrice," +
            " o.product.promotionType, o.product.discountValue " +
            "order by sum(o.quantity) desc " +
            "limit 10")
    List<ProductDisplayDto> getTopSellerProducts();

    @Query("select new " +
            "com.bravos2k5.bravosshop.dto.product.ProductDisplayDto(p.id, p.name, p.thumbnail, p.category.name, p.unitPrice, p.promotionType, p.discountValue) " +
            "from Product p " +
            "order by p.id desc " +
            "limit 10")
    List<ProductDisplayDto> getNewestProducts();

}
