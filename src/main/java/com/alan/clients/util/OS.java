package com.alan.clients.util;

public enum OS {
    LINUX,
    SOLARIS,
    WINDOWS,
    MACOS,
    UNKNOWN;

    private static final OS[] $VALUES = rW();

    OS() {
    }

    private static OS[] rW() {
        return new OS[]{LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN};
    }
}
