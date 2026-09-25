package com.alan.clients.module.impl.combat.criticals;

import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public final class NoGroundCriticals extends Mode<Criticals> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> var0.setOnGround(false);

    public NoGroundCriticals(String var1, Criticals criticals) {
        super(var1, criticals);
    }
}
