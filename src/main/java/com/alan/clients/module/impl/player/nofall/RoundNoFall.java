package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.player.FallDistanceComponent;

public class RoundNoFall extends Mode<NoFall> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        double d0 = MoveUtil.roundToGround(var0.getPosY());
        if (FallDistanceComponent.cY > 3.0F && Math.abs(d0 - var0.getPosY()) < 0.005) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, d0, aEg.thePlayer.posZ);
            var0.setOnGround(true);
            var0.setPosY(d0);
        }
    };

    public RoundNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
