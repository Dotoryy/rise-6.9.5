package com.alan.clients.util.account.dynamic;

import com.google.gson.annotations.SerializedName;

public class DynamicSkin {
    @SerializedName("id")
    private final String aHs;
    @SerializedName("state")
    private final String aHt;
    @SerializedName("url")
    private final String aHu;
    @SerializedName("variant")
    private final String aHv;

    public DynamicSkin(String var1, String var2, String var3, String var4) {
        this.aHs = var1;
        this.aHt = var2;
        this.aHu = var3;
        this.aHv = var4;
    }

    public String sY() {
        return this.aHs;
    }

    public String sZ() {
        return this.aHt;
    }

    public String mY() {
        return this.aHu;
    }

    public String td() {
        return this.aHv;
    }

    @Override
    public String toString() {
        return "DynamicSkin{id='" + this.aHs + "', state='" + this.aHt + "', url='" + this.aHu + "', variant='" + this.aHv + "'}";
    }
}
