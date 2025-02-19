package com.bravos2k5.bravosshop.product.dto;

import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.common.utils.CurrencyFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Value
public class ProductDetailDto {

    private static final CurrencyFormatter currencyFormatter = new CurrencyFormatter();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    Long id;
    String images;
    String thumbnail;
    String name;
    String description;
    Double unitPrice;
    Integer catagoryId;
    String categoryName;
    Double discountValue;
    PromotionType promotionType;

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

    public List<String> getImagesList() {
        try {
            return new ArrayList<>(objectMapper.readValue(images, new TypeReference<>() {}));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
