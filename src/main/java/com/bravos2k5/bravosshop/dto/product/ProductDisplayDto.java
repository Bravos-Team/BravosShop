package com.bravos2k5.bravosshop.dto.product;

import com.bravos2k5.bravosshop.enums.PromotionType;
import lombok.Value;

@Value
public class ProductDisplayDto {

    Long id;
    String name;
    String thumbnail;
    String categoryName;
    Double unitPrice;
    PromotionType promotionType;
    Double discountValue;

    public String getPromotionText() {
        if(promotionType == PromotionType.PERCENTAGE) {
            return "Giảm " + Math.round(discountValue) + " %";
        }
        else if(promotionType == PromotionType.FIXED) {
            return "Giảm " + Math.round(discountValue) + " VNĐ";
        }
        return "";
    }

    public Double getCurrentPrice() {
        if(promotionType == PromotionType.PERCENTAGE) {
            return unitPrice * (1 - discountValue / 100);
        }
        else if(promotionType == PromotionType.FIXED) {
            return unitPrice - discountValue;
        }
        return unitPrice;
    }

}
