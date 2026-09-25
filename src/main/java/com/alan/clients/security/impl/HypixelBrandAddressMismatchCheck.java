package com.alan.clients.security.impl;

import com.alan.clients.security.SecurityFeature;
import com.alan.clients.util.player.ServerUtil;

public class HypixelBrandAddressMismatchCheck extends SecurityFeature
{
    public volatile boolean avA;

    public boolean run() {
        if (this.avA) {
            return true;
        }
        if (HypixelBrandAddressMismatchCheck.aEg == null || HypixelBrandAddressMismatchCheck.aEg.thePlayer == null || HypixelBrandAddressMismatchCheck.aEg.theWorld == null || HypixelBrandAddressMismatchCheck.aEg.isIntegratedServerRunning()) {
            return 14 - 14 != 0;
        }
        return ServerUtil.vr() && !ServerUtil.vp() && (this.avA = true);
    }

    public String getReason() {
        return "hypixelbrandaddressmismatch";
    }

    static {
    }

}
