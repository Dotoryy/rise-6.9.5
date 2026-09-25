package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.PlayerUtil;

public class PositionAntiVoid extends Mode<AntiVoid> {
    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.fallDistance > this.distance.wo().floatValue() && !PlayerUtil.vh()) {
            var1x.setPosY(var1x.getPosY() + aEg.thePlayer.fallDistance);
        }
    };

    public PositionAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
