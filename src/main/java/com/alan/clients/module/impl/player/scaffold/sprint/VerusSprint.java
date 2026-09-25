package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;

public class VerusSprint extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.motionY = 0.0;
            MoveUtil.strafe(0.5);
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.1);
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.thePlayer.onGround) {
            var0.setSpeed(0.61);
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(0.06 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.1);
            }
        } else if (aEg.thePlayer.motionY < 0.77) {
            var0.setSpeed(0.4);
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(0.01 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.1);
            }
        }
    };

    public VerusSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
