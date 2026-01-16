package com.lankacourier.Util;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private ValidationUtil() {}

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern NIC_OLD =
            Pattern.compile("^\\d{9}[VvXx]$");

    private static final Pattern NIC_NEW =
            Pattern.compile("^\\d{12}$");

    private static final Pattern MOBILE =
            Pattern.compile("^0?7\\d{8}$");

    public static boolean isEmail(String s) {
        return s != null && EMAIL.matcher(s.trim()).matches();
    }

    public static boolean isNic(String s) {
        if (s == null) return false;
        String t = s.trim();
        return NIC_OLD.matcher(t).matches() || NIC_NEW.matcher(t).matches();
    }

    public static boolean isMobile(String s) {
        if (s == null) return false;
        String t = s.replaceAll("[^0-9]", "");
        return MOBILE.matcher(t).matches();
    }

    public static String normalizeMobile(String s) {
        if (s == null) return "";
        String t = s.replaceAll("[^0-9]", "");
        if (t.length() == 9 && t.startsWith("7"))
            t = "0" + t;
        return t;
    }

    public static boolean isValidUsername(String s) {
        return s != null && s.matches("^[A-Za-z0-9._-]{3,50}$");
    }

    // SIMPLE version (used everywhere)
    public static boolean isStrongPassword(String s) {
        return isStrongPassword(s, 8, true, true);
    }

    // CONFIGURABLE version
    public static boolean isStrongPassword(String pwd, int minLen,
                                           boolean requireDigit,
                                           boolean requireLetter) {
        if (pwd == null) return false;
        if (pwd.length() < minLen) return false;
        if (requireDigit && !pwd.matches(".*\\d.*")) return false;
        if (requireLetter && !pwd.matches(".*[A-Za-z].*")) return false;
        return true;
    }
}
