package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;

public class MatrixNoSlow extends Mode<NoSlow> {
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
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
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            if (aEg.thePlayer.cqL > 1) {
                MoveUtil.strafe(0.0265);
            } else if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX *= 0.992;
                aEg.thePlayer.motionZ *= 0.992;
            } else {
                aEg.thePlayer.motionX *= 0.99;
                aEg.thePlayer.motionZ *= 0.99;
            }
        }

        if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            if (aEg.thePlayer.cqL > 1) {
                MoveUtil.strafe(0.0265);
            } else if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX *= 0.992;
                aEg.thePlayer.motionZ *= 0.992;
            } else {
                aEg.thePlayer.motionX *= 0.99;
                aEg.thePlayer.motionZ *= 0.99;
            }
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            if (aEg.thePlayer.cqL > 1) {
                MoveUtil.strafe(0.0265);
            } else if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX *= 0.992;
                aEg.thePlayer.motionZ *= 0.992;
            } else {
                aEg.thePlayer.motionX *= 0.99;
                aEg.thePlayer.motionZ *= 0.99;
            }
        }

        if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            if (aEg.thePlayer.cqL > 1) {
                MoveUtil.strafe(0.0265);
            } else if (!this.e(Speed.class).isEnabled()) {
                aEg.thePlayer.motionX *= 0.992;
                aEg.thePlayer.motionZ *= 0.992;
            } else {
                aEg.thePlayer.motionX *= 0.99;
                aEg.thePlayer.motionZ *= 0.99;
            }
        }
    };

    public MatrixNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
