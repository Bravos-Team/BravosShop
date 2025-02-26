package com.bravos2k5.bravosshop.dto;

import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.utils.CurrencyFormatter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class ProductDisplayDto implements Serializable {

    Long id;
    String name;
    String thumbnail;
    String categoryName;
    Double unitPrice;
    PromotionType promotionType;
    Double discountValue;

    LocalDateTime startTime;
    LocalDateTime endTime;

    public boolean onPromtion() {
        return CurrencyFormatter.onPromtion(promotionType,startTime,endTime);
    }

    public String getPromotionText() {
        return CurrencyFormatter.getPromotionDisplayText(promotionType,discountValue,startTime,endTime);
    }

    public Double getCurrentPrice() {
        return CurrencyFormatter.getCurrentPrice(promotionType,unitPrice,discountValue,startTime,endTime);
    }

}
