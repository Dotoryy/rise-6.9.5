package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public class NoGroundNoFall extends Mode<NoFall> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        var0.setOnGround(false);
        var0.setPosY(var0.getPosY() + Math.random() / 1.0E20F);
    };

    public NoGroundNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
