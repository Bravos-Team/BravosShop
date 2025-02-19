package com.bravos2k5.bravosshop.product.dto;

import lombok.Value;

import java.text.DecimalFormat;

@Value
public class ProductRating {

    Double stars;

    Long reviewCount;

    public Double getAvgStars() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(stars == null ? 0 : stars));
    }

}
