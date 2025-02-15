package com.bravos2k5.bravosshop.dto.product;

import com.bravos2k5.bravosshop.enums.ProductStatus;
import com.bravos2k5.bravosshop.enums.PromotionType;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class CreateProductDto implements Serializable {

    String name;
    Integer categoryId;
    String unit;
    String description;
    Double unitPrice;
    ProductStatus status;
    PromotionType promotionType;
    Double discountValue;
    LocalDateTime startTime;
    LocalDateTime endTime;

    MultipartFile thumbnail;
    MultipartFile[] images;

}
