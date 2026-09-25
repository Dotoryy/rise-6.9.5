package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.HitSlowDownEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.Mode;

public final class IntaveVelocity extends Mode<Velocity> {
    private boolean attacked;
    private boolean slowedDown;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (this.attacked && !this.slowedDown && aEg.thePlayer.isSprinting()) {
                aEg.thePlayer.motionX *= 0.6;
                aEg.thePlayer.motionZ *= 0.6;
                aEg.thePlayer.setSprinting(false);
            }

            this.attacked = false;
            this.slowedDown = false;
        }
    };
    @EventLink
    public final Listener<HitSlowDownEvent> onHitSlowDown = var1x -> this.slowedDown = true;
    @EventLink
    public final Listener<AttackEvent> onAttack = var1x -> this.attacked = true;

    public IntaveVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
