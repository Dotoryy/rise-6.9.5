package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;

public class CollisionAntiVoid extends Mode<AntiVoid> {
    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (FallDistanceComponent.cY > this.distance.wo().intValue() && !PlayerUtil.vh() && aEg.thePlayer.posY + aEg.thePlayer.motionY < Math.floor(aEg.thePlayer.posY)) {
            aEg.thePlayer.motionY = Math.floor(aEg.thePlayer.posY) - aEg.thePlayer.posY;
            if (aEg.thePlayer.motionY == 0.0) {
                aEg.thePlayer.onGround = true;
                var1x.setOnGround(true);
            }
        }
    };

    public CollisionAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
