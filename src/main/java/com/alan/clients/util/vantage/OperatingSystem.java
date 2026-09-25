package com.alan.clients.util.vantage;

public enum OperatingSystem {
    WINDOWS("Windows"),
    MACOSX("MacOS"),
    LINUX("Linux");

    private final String os;
    private static final OperatingSystem[] $VALUES = wd();

    OperatingSystem(String var3) {
        this.os = var3;
    }

    public String getOs() {
        return this.os;
    }

    private static OperatingSystem[] wd() {
        return new OperatingSystem[]{WINDOWS, MACOSX, LINUX};
    }
}
