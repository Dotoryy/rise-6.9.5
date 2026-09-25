package com.alan.clients.util;

public class TimeFormatUtil {
    public TimeFormatUtil() {
    }

    public static String b(long var0, boolean var2) {
        if (var0 <= 0L) {
            return var2 ? "0.0s" : "0s";
        }

        String s;
        if (var0 < 60000L) {
            s = var2 ? String.format("%.1fs", var0 / 1000.0) : var0 / 1000L + "s";
        } else if (var0 < 3600000L) {
            s = var0 / 60000L + "m, " + var0 % 60000L / 1000L + "s";
        } else if (var0 < 86400000L) {
            s = var0 / 3600000L + "h, " + var0 % 3600000L / 60000L + "m, " + var0 % 60000L / 1000L + "s";
        } else {
            s = var0 / 86400000L + "d, " + var0 % 86400000L / 3600000L + "h, " + var0 % 3600000L / 60000L + "m, " + var0 % 60000L / 1000L + "s";
        }

        return s;
    }
}
