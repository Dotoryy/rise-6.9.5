package com.alan.clients.module.impl.movement.wallclimb;

import com.alan.clients.module.impl.movement.WallClimb;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public class VerusWallClimb extends Mode<WallClimb> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.thePlayer.isCollidedHorizontally && aEg.thePlayer.ticksExisted % 2 == 0) {
            aEg.thePlayer.jump();
        }
    };

    public VerusWallClimb(String var1, WallClimb wallClimb) {
        super(var1, wallClimb);
    }
}
