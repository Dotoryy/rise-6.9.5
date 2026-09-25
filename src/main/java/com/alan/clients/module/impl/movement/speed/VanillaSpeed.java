package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public class VanillaSpeed extends Mode<Speed> {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (MoveUtil.isMoving() && aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }

        var1x.setSpeed(this.speed.wo().floatValue());
    };

    public VanillaSpeed(String var1, Speed var2) {
        super(var1, var2);
    }
}
