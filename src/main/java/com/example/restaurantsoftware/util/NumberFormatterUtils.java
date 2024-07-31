package com.example.restaurantsoftware.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatterUtils {
    public static String formatQuantity(Double quantity) {
        if (quantity == null) {
            return null;
        }
        BigDecimal bd = new BigDecimal(quantity).setScale(3, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(bd);
    }
}
