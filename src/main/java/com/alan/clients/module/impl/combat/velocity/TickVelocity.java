package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public final class TickVelocity extends Mode<Velocity> {
    private final NumberValue tickVelocity = new NumberValue("Tick Velocity", this, 1, 1, 6, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (aEg.thePlayer.hurtTime == 10 - this.tickVelocity.wo().intValue()) {
                MoveUtil.stop();
            }
        }
    };

    public TickVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
