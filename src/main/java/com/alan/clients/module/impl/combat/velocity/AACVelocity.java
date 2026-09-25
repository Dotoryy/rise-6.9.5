package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.combat.TargetComponent;

public final class AACVelocity extends Mode<Velocity> {
    private boolean jump;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            TargetComponent.f(7.0);
            if (aEg.thePlayer.onGround && aEg.thePlayer.hurtTime > 0 && !BadPacketsComponent.bad(false, true, false, false, false)) {
                aEg.thePlayer.motionX *= 0.6;
                aEg.thePlayer.motionZ *= 0.6;
            }

            this.jump = false;
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (this.jump) {
                var1x.setJump(true);
            }
        }
    };

    public AACVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
