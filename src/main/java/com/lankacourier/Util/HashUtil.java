package com.lankacourier.Util;

import at.favre.lib.crypto.bcrypt.BCrypt;


public final class HashUtil {

    public static final int DEFAULT_COST = 10;

    private HashUtil() {}

    public static String hashPassword(String plainPassword) {
        return hashPassword(plainPassword, DEFAULT_COST);
    }


    public static String hashPassword(String plainPassword, int cost) {
        if (plainPassword == null) throw new IllegalArgumentException("password cannot be null");
        if (cost < 4 || cost > 31) throw new IllegalArgumentException("cost must be between 4 and 31");
        // uses automatically generated random salt of 16 bytes internally
        return BCrypt.withDefaults().hashToString(cost, plainPassword.toCharArray());
    }

 
    public static boolean verifyPassword(String plainPassword, String bcryptHash) {
        if (plainPassword == null || bcryptHash == null) return false;
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), bcryptHash);
        return result.verified;
    }
    public static boolean looksLikeBcryptHash(String s) {
        if (s == null) return false;
        // bcrypt hash starts with $2a$, $2b$ or $2y$ followed by cost
        return s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$");
    }

    public static int getCostFromBcryptHash(String bcryptHash) {
        if (!looksLikeBcryptHash(bcryptHash)) return -1;
        try {
            // hash format: $2b$10$...  -> split on '$' and parse 10
            String[] parts = bcryptHash.split("\\$");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[2]);
            }
        } catch (Exception ignored) {}
        return -1;
    }
}
