package com.bravos2k5.bravosshop.product.dto;

import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.common.utils.CurrencyFormatter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class ProductDisplayDto implements Serializable {

    private static final CurrencyFormatter currencyFormatter = new CurrencyFormatter();

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
        LocalDateTime now = LocalDateTime.now();
        return promotionType != PromotionType.NO_PROMOTION &&
                startTime != null && endTime != null &&
                startTime.isBefore(now) && endTime.isAfter(now);
    }

    public String getPromotionText() {
        if (onPromtion()) {
            if(promotionType == PromotionType.PERCENTAGE) {
                return "Giảm " + Math.round(discountValue) + " %";
            }
            else if(promotionType == PromotionType.FIXED) {
                String money = currencyFormatter.formatToVietnameseCurrency(discountValue);
                return "Giảm " + money;
            }
        }
        return "";
    }

    public Double getCurrentPrice() {
        if (onPromtion()) {
            if(promotionType == PromotionType.PERCENTAGE) {
                return unitPrice * (1 - discountValue / 100);
            }
            else if(promotionType == PromotionType.FIXED) {
                return unitPrice - discountValue;
            }
        }
        return unitPrice;
    }

}
