package com.alan.clients.module.impl.render.fullbright;

import com.alan.clients.module.impl.render.FullBright;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public final class EffectFullBright extends Mode<FullBright> {
    @EventLink
    public final Listener<TickEvent> onTick = var0 -> aEg.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, Integer.MAX_VALUE, 1));

    public EffectFullBright(String var1, FullBright fullBright) {
        super(var1, fullBright);
    }

    @Override
    public void onDisable() {
        if (aEg.thePlayer.isPotionActive(Potion.nightVision)) {
            aEg.thePlayer.removePotionEffect(Potion.nightVision.id);
        }
    }
}
