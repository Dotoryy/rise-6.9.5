package com.alan.clients.util.account.localts;

import com.google.gson.JsonArray;

public final class LocaltsOrder {
    public final boolean aFp;
    public final String aFq;
    public final String aFr;
    public final String aFs;
    public final JsonArray aFt;
    public final String aFu;

    LocaltsOrder(boolean var1, String var2, String var3, String var4, JsonArray var5, String var6) {
        this.aFp = var1;
        this.aFq = var2;
        this.aFr = var3;
        this.aFs = var4;
        this.aFt = var5;
        this.aFu = var6;
    }
}
