package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;

public class VariableNoSlow extends Mode<NoSlow> {
    private final NumberValue multiplier = new NumberValue("Multiplier", this, 0.8, 0.2, 1, 0.05);
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            var1x.setForwardMultiplier(this.multiplier.wo().floatValue());
            var1x.setStrafeMultiplier(this.multiplier.wo().floatValue());
        }

        if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            var1x.setForwardMultiplier(this.multiplier.wo().floatValue());
            var1x.setStrafeMultiplier(this.multiplier.wo().floatValue());
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            var1x.setForwardMultiplier(this.multiplier.wo().floatValue());
            var1x.setStrafeMultiplier(this.multiplier.wo().floatValue());
        }

        if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            var1x.setForwardMultiplier(this.multiplier.wo().floatValue());
            var1x.setStrafeMultiplier(this.multiplier.wo().floatValue());
        }
    };

    public VariableNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
