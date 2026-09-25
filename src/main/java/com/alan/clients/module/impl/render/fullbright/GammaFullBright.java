package com.alan.clients.module.impl.render.fullbright;

import com.alan.clients.module.impl.render.FullBright;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;

public final class GammaFullBright extends Mode<FullBright> {
    private float oldGamma;
    @EventLink
    public final Listener<TickEvent> onTick = var0 -> aEg.gameSettings.gammaSetting = 100.0F;

    public GammaFullBright(String var1, FullBright fullBright) {
        super(var1, fullBright);
    }

    @Override
    public void onEnable() {
        this.oldGamma = aEg.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        aEg.gameSettings.gammaSetting = this.oldGamma;
    }
}
