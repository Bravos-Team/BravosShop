package com.bravos2k5.bravosshop.dto;

import com.bravos2k5.bravosshop.enums.PromotionType;
import com.bravos2k5.bravosshop.utils.CurrencyFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        return CurrencyFormatter.onPromtion(promotionType,startTime,endTime);
    }

    public String getPromotionText() {
        return CurrencyFormatter.getPromotionDisplayText(promotionType,discountValue,startTime,endTime);
    }

    public Double getCurrentPrice() {
        return CurrencyFormatter.getCurrentPrice(promotionType,unitPrice,discountValue,startTime,endTime);
    }

    public List<String> getImagesList() {
        try {
            return new ArrayList<>(objectMapper.readValue(images, new TypeReference<>() {}));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

}
