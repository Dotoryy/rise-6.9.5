package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public class JumpStep extends Mode<Step> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.thePlayer.onGround && aEg.thePlayer.isCollidedHorizontally) {
            aEg.thePlayer.jump();
        }
    };

    public JumpStep(String var1, Step step) {
        super(var1, step);
    }
}
