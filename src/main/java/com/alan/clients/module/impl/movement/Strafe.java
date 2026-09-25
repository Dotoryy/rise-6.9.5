package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.math.MathUtil;
import net.minecraft.util.MathHelper;

@ModuleInfo(aliases = "module.movement.strafe.name", description = "module.movement.strafe.description", category = Category.MOVEMENT)
public class Strafe extends Module {
    private NumberValue strength = new NumberValue("Strength", this, 100, 1, 100, 1);
    public final BooleanValue hypixelFlyDisabler = new BooleanValue("Hypixel Fly Disabler", this, false);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1 -> {
        if (!this.hypixelFlyDisabler.wo()) {
            MoveUtil.partialStrafePercent(this.strength.wo().floatValue());
        }
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = var1 -> {
        double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
        double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        if (this.hypixelFlyDisabler.wo()) {
            if (!aEg.thePlayer.onGround && aEg.thePlayer.tR != 1 && aEg.thePlayer.ae >= 20) {
                if (aEg.thePlayer.ae <= 1 || !(MathUtil.n(d0, d1) > 90.0)) {
                    double d2 = aEg.thePlayer.motionX;
                    double d3 = aEg.thePlayer.motionZ;
                    MoveUtil.strafe();
                    aEg.thePlayer.motionZ = (aEg.thePlayer.motionZ * 2.1 + d3 * 1.0) / 3.1;
                    aEg.thePlayer.motionX = (aEg.thePlayer.motionX * 2.1 + d2 * 1.0) / 3.1;
                } else if (MathUtil.n(d0, d1) > 90.0) {
                    MoveUtil.a(MoveUtil.speed(), (float)d1 - 180.0F);
                }
            } else {
                MoveUtil.strafe();
            }
        }
    };

    public Strafe() {
    }

    @Override
    public void onDisable() {
        aEg.timer.dzD = 1.0F;
    }
}
