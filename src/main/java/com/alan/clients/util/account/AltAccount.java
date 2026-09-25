package com.alan.clients.util.account;

import com.google.gson.JsonObject;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltAccount {
    private AltType aEK;
    private String gK;
    private String aEL;
    private String aEM;
    private long aEN;

    public AltAccount(AltType altType, String var2, String var3, String var4) {
        this.aEK = altType;
        this.gK = var2;
        this.aEL = var3;
        this.aEM = var4;
    }

    public boolean se() {
        Minecraft.getMinecraft().setSession(new Session(this.gK, this.aEL, this.aEM, "mojang"));
        this.aEN = System.currentTimeMillis();
        return true;
    }

    public boolean kW() {
        return this.gK != null && this.aEL != null && this.aEM != null && !this.gK.isEmpty() && !this.aEL.isEmpty() && !this.aEM.isEmpty();
    }

    public JsonObject sf() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("type", this.aEK.getName());
        jsonobject.addProperty("name", this.gK);
        jsonobject.addProperty("uuid", this.aEL);
        jsonobject.addProperty("accessToken", this.aEM);
        jsonobject.addProperty("lastUsed", this.aEN);
        return jsonobject;
    }

    public void b(JsonObject json) {
        if (json.has("type")) {
            this.aEK = AltType.bi(json.get("type").getAsString());
        } else {
            this.aEK = AltType.CRACKED;
        }

        if (json.has("name")) {
            this.gK = json.get("name").getAsString();
        }

        if (json.has("uuid")) {
            this.aEL = json.get("uuid").getAsString();
        }

        if (json.has("accessToken")) {
            this.aEM = json.get("accessToken").getAsString();
        }

        if (json.has("lastUsed")) {
            this.aEN = json.get("lastUsed").getAsLong();
        }
    }

    @Generated
    public AltType sg() {
        return this.aEK;
    }

    @Generated
    public String getName() {
        return this.gK;
    }

    @Generated
    public String sh() {
        return this.aEL;
    }

    @Generated
    public String si() {
        return this.aEM;
    }

    @Generated
    public long sj() {
        return this.aEN;
    }

    @Generated
    public void a(AltType altType) {
        this.aEK = altType;
    }

    @Generated
    public void setName(String name) {
        this.gK = name;
    }

    @Generated
    public void bg(String var1) {
        this.aEL = var1;
    }

    @Generated
    public void bh(String var1) {
        this.aEM = var1;
    }

    @Generated
    public void e(long var1) {
        this.aEN = var1;
    }
}
