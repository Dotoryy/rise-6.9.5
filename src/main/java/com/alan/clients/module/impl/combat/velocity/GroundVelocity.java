package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public final class GroundVelocity extends Mode<Velocity> {
    private final NumberValue delay = new NumberValue("Delay", this, 1, 0, 20, 1);
    private int ticks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (aEg.thePlayer.ae == this.delay.wo().intValue()) {
                aEg.thePlayer.onGround = true;
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var1x -> {
        if (aEg.thePlayer.ae == this.delay.wo().intValue() + 1) {
            var1x.setJump(false);
        }
    };

    public GroundVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
