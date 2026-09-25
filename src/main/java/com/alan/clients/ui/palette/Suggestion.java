package com.alan.clients.ui.palette;

final class Suggestion {
    final String aBz;
    final String aBA;
    final String aBB;
    final String aBC;
    final int aBD;
    final boolean aBE;

    Suggestion(String var1, String var2, String var3, String var4, int var5, boolean var6) {
        this.aBz = var1;
        this.aBA = var2 == null ? "" : var2;
        this.aBB = var3 == null ? "" : var3;
        this.aBC = var4;
        this.aBD = var5;
        this.aBE = var6;
    }
}
