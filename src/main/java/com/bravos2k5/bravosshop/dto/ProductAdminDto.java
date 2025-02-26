package com.bravos2k5.bravosshop.dto;

import com.bravos2k5.bravosshop.enums.ProductStatus;
import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.utils.CurrencyFormatter;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class ProductAdminDto {

    Long id;
    String name;
    String categoryName;
    Double unitPrice;

    PromotionType promotionType;
    Double discountValue;

    ProductStatus productStatus;

    LocalDateTime startTime;
    LocalDateTime endTime;

    public String getPromotionInfo() {
        return CurrencyFormatter.getPromotionInfo(promotionType,startTime,endTime,discountValue);
    }

}
