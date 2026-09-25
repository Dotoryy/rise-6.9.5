package com.alan.clients.module.impl.movement.sneak;

import com.alan.clients.module.impl.movement.Sneak;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public class StandardSneak extends Mode<Sneak> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> aEg.thePlayer.movementInput.sneak = aEg.thePlayer.sendQueue.doneLoadingTerrain;

    public StandardSneak(String var1, Sneak sneak) {
        super(var1, sneak);
    }
}
