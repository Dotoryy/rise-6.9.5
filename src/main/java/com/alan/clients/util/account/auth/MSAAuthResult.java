package com.alan.clients.util.account.auth;

public final class MSAAuthResult {
    public final String aFj;
    public final int aFk;

    MSAAuthResult(String var1, int var2) {
        this.aFj = var1;
        this.aFk = var2;
    }

    public boolean sr() {
        return this.aFj != null && !this.aFj.isEmpty();
    }
}
