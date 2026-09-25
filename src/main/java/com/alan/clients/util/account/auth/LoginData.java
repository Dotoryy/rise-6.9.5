package com.alan.clients.util.account.auth;

public class LoginData {
    public String aEX;
    public String aEY;
    public String aEL;
    public String aCj;

    public LoginData() {
    }

    public LoginData(String var1, String var2, String var3, String var4) {
        this.aEX = var1;
        this.aEY = var2;
        this.aEL = var3;
        this.aCj = var4;
    }

    public boolean sm() {
        return this.aEX != null;
    }
}
