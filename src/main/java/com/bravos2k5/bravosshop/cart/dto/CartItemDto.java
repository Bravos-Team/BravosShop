package com.bravos2k5.bravosshop.cart.dto;

import com.bravos2k5.bravosshop.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CartItemDto {

    Long cartItemId;

    String name;

    String categoryName;

    String thumbnail;

    Long quantity;

    Double unitPrice;

    Double discountValue;

    PromotionType promotionType;

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
