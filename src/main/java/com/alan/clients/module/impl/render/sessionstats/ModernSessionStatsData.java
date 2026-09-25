package com.alan.clients.module.impl.render.sessionstats;

import lombok.Generated;

public class ModernSessionStatsData {
    public int kills;
    public int wins;
    int userBans;
    int globalBans;
    double distanceWalked;
    double distanceFlown;
    public final long startTime = System.currentTimeMillis();

    @Generated
    public ModernSessionStatsData(int var1, int var2, int var3, int var4, double var5, double var7) {
        this.kills = var1;
        this.wins = var2;
        this.userBans = var3;
        this.globalBans = var4;
        this.distanceWalked = var5;
        this.distanceFlown = var7;
    }
}
