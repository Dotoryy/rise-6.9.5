package com.alan.clients.util.account.dynamic;

import com.google.gson.annotations.SerializedName;
import com.alan.clients.util.account.dynamic.DynamicCape;
import java.util.Arrays;

public class DynamicAccountResult {
    @SerializedName("id")
    private final int aHn;
    @SerializedName("name")
    private final String name;
    @SerializedName("uuid")
    private final String aHp;
    @SerializedName("skins")
    private final DynamicSkin[] aHq;
    @SerializedName("capes")
    private final DynamicCape[] aHr;

    public DynamicAccountResult(int var1, String var2, String var3, DynamicSkin[] var4, DynamicCape[] var5) {
        this.aHn = var1;
        this.name = var2;
        this.aHp = var3;
        this.aHq = var4;
        this.aHr = var5;
    }

    public int getId() {
        return this.aHn;
    }

    public String getName() {
        return this.name;
    }

    public String sh() {
        return this.aHp;
    }

    public DynamicSkin[] tb() {
        return this.aHq;
    }

    public DynamicCape[] tc() {
        return this.aHr;
    }

    @Override
    public String toString() {
        return "DynamicAccountResult{id="
            + this.aHn
            + ", name='"
            + this.name
            + "', uuid='"
            + this.aHp
            + "', skins="
            + Arrays.toString(this.aHq)
            + ", capes="
            + Arrays.toString(this.aHr)
            + "}";
    }
}
