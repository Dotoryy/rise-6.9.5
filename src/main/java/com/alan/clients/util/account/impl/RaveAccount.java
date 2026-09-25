package com.alan.clients.util.account.impl;

import com.google.gson.JsonObject;
import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.account.AltType;
import lombok.Generated;

public class RaveAccount extends AltAccount {
    private String aFf;
    private long aFg;

    public RaveAccount(String var1, String var2, String var3, String var4) {
        super(AltType.RAVE, var1, var2, var3);
        this.aFf = var4;
        this.aFg = System.currentTimeMillis();
    }

    @Override
    public boolean kW() {
        return super.kW() && this.aFf != null && !this.aFf.isEmpty();
    }

    @Override
    public JsonObject sf() {
        JsonObject jsonobject = super.sf();
        jsonobject.addProperty("orderId", this.aFf);
        jsonobject.addProperty("purchaseDate", this.aFg);
        return jsonobject;
    }

    @Override
    public void b(JsonObject json) {
        super.b(json);
        if (json.has("orderId")) {
            this.aFf = json.get("orderId").getAsString();
        }

        if (json.has("purchaseDate")) {
            this.aFg = json.get("purchaseDate").getAsLong();
        }
    }

    @Generated
    public String sp() {
        return this.aFf;
    }

    @Generated
    public long sq() {
        return this.aFg;
    }

    @Generated
    public void bn(String var1) {
        this.aFf = var1;
    }

    @Generated
    public void f(long var1) {
        this.aFg = var1;
    }
}
