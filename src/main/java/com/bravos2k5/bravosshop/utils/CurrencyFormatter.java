package com.bravos2k5.bravosshop.utils;

import com.bravos2k5.bravosshop.enums.PromotionType;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class CurrencyFormatter {

    public static String formatToVietnameseCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("vi", "vn"));
        return currencyFormatter.format(amount);
    }

    public static boolean onPromtion(PromotionType promotionType, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        return promotionType != PromotionType.NO_PROMOTION &&
                startTime != null && endTime != null &&
                startTime.isBefore(now) && endTime.isAfter(now);
    }

    public static String getPromotionDisplayText(PromotionType promotionType,
                                                 Double discountValue,
                                                 LocalDateTime startTime,
                                                 LocalDateTime endTime) {
        if (onPromtion(promotionType,startTime,endTime)) {
            if(promotionType == PromotionType.PERCENTAGE) {
                return "Giảm " + Math.round(discountValue) + " %";
            }
            else if(promotionType == PromotionType.FIXED) {
                String money = CurrencyFormatter.formatToVietnameseCurrency(discountValue);
                return "Giảm " + money;
            }
        }
        return "";
    }

    public static Double getCurrentPrice(PromotionType promotionType,
                                         Double unitPrice,
                                         Double discountValue,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime) {
        if (onPromtion(promotionType,startTime,endTime)) {
            if(promotionType == PromotionType.PERCENTAGE) {
                return unitPrice * (1 - discountValue / 100);
            }
            else if(promotionType == PromotionType.FIXED) {
                return unitPrice - discountValue;
            }
        }
        return unitPrice;
    }

    public static String getPromotionInfo(PromotionType promotionType, LocalDateTime startTime, LocalDateTime endTime, Double discountValue) {
        LocalDateTime now = LocalDateTime.now();
        if(promotionType == PromotionType.NO_PROMOTION) return "Không";
        else if(startTime == null || startTime.isAfter(now)) {
            return "Chưa bắt đầu";
        } else if (endTime == null || endTime.isBefore(now)) {
            return "Đã kết thúc";
        }
        if(promotionType == PromotionType.PERCENTAGE) {
            return "Giảm " + Math.round(discountValue) + " %";
        }
        else {
            String money = CurrencyFormatter.formatToVietnameseCurrency(discountValue);
            return "Giảm " + money;
        }
    }

}
