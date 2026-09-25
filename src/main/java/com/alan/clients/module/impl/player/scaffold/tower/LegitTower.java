package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.value.Mode;

public class LegitTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown()) {
            if (aEg.thePlayer.onGround) {
                aEg.thePlayer.jump();
            }
        }
    };

    public LegitTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
