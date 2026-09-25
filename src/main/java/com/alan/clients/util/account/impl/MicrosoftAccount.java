package com.alan.clients.util.account.impl;

import com.alan.clients.util.account.auth.MicrosoftLogin;
import com.alan.clients.util.account.auth.LoginData;
import com.google.gson.JsonObject;
import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.account.AltType;
import lombok.Generated;

public class MicrosoftAccount extends AltAccount {
    private String aFe;

    public MicrosoftAccount(String var1, String var2, String var3, String var4) {
        super(AltType.MICROSOFT, var1, var2, var3);
        this.aFe = var4;
    }

    public static MicrosoftAccount sn() {
        MicrosoftAccount aep = new MicrosoftAccount("", "", "", "");
        MicrosoftLogin.getRefreshToken(var1x -> {
            aep.bm(var1x);
            aep.se();
        });
        return aep;
    }

    @Override
    public boolean se() {
        if (this.aFe.isEmpty()) {
            return super.se();
        }

        LoginData d = MicrosoftLogin.login(this.aFe);
        if (!d.sm()) {
            d = MicrosoftLogin.loginMarketplaceRefreshToken(this.aFe);
        }

        if (!d.sm()) {
            return false;
        }

        this.setName(d.aCj);
        this.bg(d.aEL);
        this.bh(d.aEX);
        this.bm(d.aEY);
        return super.se();
    }

    @Override
    public boolean kW() {
        return super.kW();
    }

    @Override
    public JsonObject sf() {
        JsonObject jsonobject = super.sf();
        jsonobject.addProperty("refreshToken", this.aFe);
        return jsonobject;
    }

    @Override
    public void b(JsonObject json) {
        super.b(json);
        if (json.has("refreshToken")) {
            this.aFe = json.get("refreshToken").getAsString();
        }
    }

    @Generated
    public String so() {
        return this.aFe;
    }

    @Generated
    public void bm(String var1) {
        this.aFe = var1;
    }
}
