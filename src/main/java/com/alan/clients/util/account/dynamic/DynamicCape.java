package com.alan.clients.util.account.dynamic;

import com.google.gson.annotations.SerializedName;

public class DynamicCape {
    @SerializedName("id")
    private final String aHj;
    @SerializedName("state")
    private final String aHk;
    @SerializedName("url")
    private final String aHl;
    @SerializedName("alias")
    private final String aHm;

    public DynamicCape(String var1, String var2, String var3, String var4) {
        this.aHj = var1;
        this.aHk = var2;
        this.aHl = var3;
        this.aHm = var4;
    }

    public String sY() {
        return this.aHj;
    }

    public String sZ() {
        return this.aHk;
    }

    public String mY() {
        return this.aHl;
    }

    public String ta() {
        return this.aHm;
    }
}
