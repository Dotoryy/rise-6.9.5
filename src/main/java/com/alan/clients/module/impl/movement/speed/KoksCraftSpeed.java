package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class KoksCraftSpeed extends Mode<Speed> {
    int jumps;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.onGround) {
            if (aEg.thePlayer.hurtTime == 0) {
                MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * 0.99);
            }

            aEg.thePlayer.jump();
            this.jumps++;
        }

        if (aEg.thePlayer.tR == 1 && aEg.thePlayer.hurtTime == 0) {
            aEg.thePlayer.motionY = MoveUtil.predictedMotion(aEg.thePlayer.motionY, this.jumps % 2 == 0 ? 2 : 4);
        }
    };

    public KoksCraftSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    @Override
    public void onEnable() {
        this.jumps = 0;
    }
}
