package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.value.Mode;

public class DisabledSprint extends Mode<Scaffold> {
    @EventLink(value = 0)
    public final Listener<StrafeEvent> onPreMotionEvent = var0 -> {
        aEg.gameSettings.cgG.setPressed(false);
        aEg.thePlayer.setSprinting(false);
    };

    public DisabledSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
