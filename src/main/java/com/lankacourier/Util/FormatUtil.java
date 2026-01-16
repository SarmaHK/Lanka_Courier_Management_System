package com.lankacourier.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class FormatUtil {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,##0.00");
    private static final DecimalFormat WEIGHT_FORMAT = new DecimalFormat("0.000");

    private FormatUtil() {} 
    //   Format a BigDecimal price into two decimal places, comma separated.
    public static String formatPrice(BigDecimal price) {
        if (price == null) return "";
        return PRICE_FORMAT.format(price.setScale(2, RoundingMode.HALF_UP));
    }

    // Convert user text input into BigDecimal price safely.
     
    public static BigDecimal parsePrice(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            BigDecimal bd = new BigDecimal(s.trim());
            return bd.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }


    
    // Format weight with 3 decimal places.
    public static String formatWeight(BigDecimal weight) {
        if (weight == null) return "";
        return WEIGHT_FORMAT.format(weight.setScale(3, RoundingMode.HALF_UP));
    }

    //  Parse user text into BigDecimal weight.
    public static BigDecimal parseWeight(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            BigDecimal bd = new BigDecimal(s.trim());
            return bd.setScale(3, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }

    // Convert and display mobile numbers in clean format
    public static String formatMobile(String s) {
        if (s == null) return "";
        s = s.replaceAll("[^0-9]", "");
        if (s.startsWith("94")) 
            s = s.substring(2);
        if (!s.startsWith("0") && s.length() == 9)
             s = "0" + s;
        return s;
    }


    //  Helps avoid NullPointerException when printing text or using in UI components.
    public static String safe(String s) {
        return s == null ? "" : s;
    }
}
