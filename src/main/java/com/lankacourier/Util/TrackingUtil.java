package com.lankacourier.Util;

import java.security.SecureRandom;
import java.util.Locale;

public final class TrackingUtil {

    private static final SecureRandom RNG = new SecureRandom();

    private TrackingUtil() {}

    public static String generateTrackingId() {
        long ts = System.currentTimeMillis();
        int r = RNG.nextInt(0xFFF);
        return String.format(Locale.ROOT, "LC%X%03X", ts, r);
    }
}
