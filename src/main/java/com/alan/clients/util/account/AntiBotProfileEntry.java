package com.alan.clients.util.account;

class AntiBotProfileEntry {
    final boolean aEI;
    private final long aEJ;

    public AntiBotProfileEntry(boolean var1) {
        this.aEI = var1;
        this.aEJ = System.currentTimeMillis();
    }

    public boolean sd() {
        return System.currentTimeMillis() - this.aEJ > AntiBotProfileLookup.aEF;
    }
}
