package com.bravos2k5.bravosshop.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public String formatToVietnameseCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("vi","vn"));
        return currencyFormatter.format(amount);
    }

}
