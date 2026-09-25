package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;

public final class BounceVelocity extends Mode<Velocity> {
    private final NumberValue tick = new NumberValue("Tick", this, 0, 0, 6, 1);
    private final BooleanValue vertical = new BooleanValue("Vertical", this, false);
    private final BooleanValue horizontal = new BooleanValue("Horizontal", this, false);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (aEg.thePlayer.hurtTime == 9 - this.tick.wo().intValue()) {
                if (this.horizontal.wo()) {
                    if (MoveUtil.isMoving()) {
                        MoveUtil.strafe();
                    } else {
                        aEg.thePlayer.motionZ *= -1.0;
                        aEg.thePlayer.motionX *= -1.0;
                    }
                }

                if (this.vertical.wo()) {
                    aEg.thePlayer.motionY *= -1.0;
                }
            }
        }
    };

    public BounceVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
