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

public class PredictionNoSlow extends Mode<NoSlow> {
    private final NumberValue amount = new NumberValue("Amount", this, 2, 2, 5, 1);
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (aEg.thePlayer.cqL % this.amount.wo().intValue() != 0 && aEg.thePlayer.onGround) {
            if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                var1x.setCancelled();
            }

            if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
                var1x.setCancelled();
            }

            if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                var1x.setCancelled();
            }

            if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                var1x.setCancelled();
            }
        }
    };

    public PredictionNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
