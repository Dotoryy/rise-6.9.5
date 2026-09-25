package com.alan.clients.module.impl.movement.wallclimb;

import com.alan.clients.module.impl.movement.WallClimb;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class MineMenClubWallClimb extends Mode<WallClimb> {
    private boolean hitHead;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.isCollidedHorizontally && !this.hitHead && aEg.thePlayer.ticksExisted % 3 == 0) {
            aEg.thePlayer.motionY = MoveUtil.jumpMotion();
        }

        if (aEg.thePlayer.isCollidedVertically) {
            this.hitHead = !aEg.thePlayer.onGround;
        }
    };

    public MineMenClubWallClimb(String var1, WallClimb wallClimb) {
        super(var1, wallClimb);
    }
}
