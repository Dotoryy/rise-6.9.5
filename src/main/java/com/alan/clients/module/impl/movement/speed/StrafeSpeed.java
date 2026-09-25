package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;

public final class StrafeSpeed extends Mode<Speed> {
    private final BooleanValue hurtBoost = new BooleanValue("Hurt Boost", this, false);
    private final NumberValue boostSpeed = new NumberValue("Boost Speed", this, 1, 0.1, 9.5, 0.1);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (!MoveUtil.isMoving()) {
            MoveUtil.stop();
        } else {
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.jump();
            }

            if (this.hurtBoost.wo() && aEg.thePlayer.hurtTime == 9) {
                MoveUtil.strafe(this.boostSpeed.wo().doubleValue());
            }

            MoveUtil.strafe();
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var0 -> var0.setJump(false);

    public StrafeSpeed(String var1, Speed speed) {
        super(var1, speed);
    }
}
