package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.NumberValue;
import java.util.HashMap;
import net.minecraft.potion.PotionEffect;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.movement.potionextender.name", description = "module.movement.potionextender.description", category = Category.MOVEMENT)
public class PotionExtender extends Module {
    private final NumberValue extendDuration = new NumberValue("Extend Duration", this, 10, 1, 60, 1);
    public final HashMap<PotionEffect, StopWatch> potions = new HashMap<>();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        for (PotionEffect potioneffect : aEg.thePlayer.getActivePotionEffects()) {
            if (this.potions.containsKey(potioneffect)) {
                StopWatch ax = this.potions.get(potioneffect);
                if (ax.T(this.extendDuration.wo().longValue() * 1000L)) {
                    aEg.thePlayer.removePotionEffect(potioneffect.getPotionID());
                    this.potions.remove(potioneffect);
                }
            } else if (potioneffect.duration == 1) {
                StopWatch a = new StopWatch();
                a.aX();
                this.potions.put(potioneffect, a);
            }
        }
    };

    public PotionExtender() {
    }
}
