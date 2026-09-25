package com.alan.clients.module.impl.movement.jesus;

import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;

public class VulcanDolphinJesus extends Mode<Jesus> {
    private int ticksSinceWater = 0;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.isInWater()) {
            this.ticksSinceWater = 0;
            MoveUtil.strafe(0.335 - Math.random() / 1000.0);
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                MoveUtil.strafe(0.033 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.34 - Math.random() / 1000.0);
            }

            this.ticksSinceWater++;
        }

        this.ticksSinceWater++;
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.isInWater() && this.ticksSinceWater < 10) {
            aEg.thePlayer.motionY = 0.01 - Math.random() / 1000.0;
            this.ticksSinceWater++;
        }
    };

    public VulcanDolphinJesus(String var1, Jesus jesus) {
        super(var1, jesus);
    }
}
