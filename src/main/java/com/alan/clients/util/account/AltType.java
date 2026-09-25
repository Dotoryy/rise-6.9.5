package com.alan.clients.util.account;

public enum AltType {
    CRACKED("Cracked"),
    MICROSOFT("Microsoft"),
    RAVE("Rave");

    private final String aER;
    private static final AltType[] $VALUES = sk();

    AltType(String var3) {
        this.aER = var3;
    }

    public static AltType bi(String var0) {
        for (AltType altType : values()) {
            if (altType.getName().equalsIgnoreCase(var0)) {
                return altType;
            }
        }

        return null;
    }

    public String getName() {
        return this.aER;
    }

    private static AltType[] sk() {
        return new AltType[]{CRACKED, MICROSOFT, RAVE};
    }
}
