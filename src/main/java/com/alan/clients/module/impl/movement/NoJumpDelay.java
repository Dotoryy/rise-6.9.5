package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;

@ModuleInfo(aliases = "module.movement.nojumpdelay.name", description = "module.movement.nojumpdelay.description", category = Category.MOVEMENT)
public class NoJumpDelay extends Module {
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        Scaffold scaffold = this.e(Scaffold.class);
        if (!scaffold.isEnabled() || !scaffold.mode.wo().getName().equals("Telly") || !aEg.gameSettings.keyBindJump.isKeyDown() || !scaffold.shouldDelayTellyJump()) {
            aEg.thePlayer.jumpTicks = 0;
        }
    };

    public NoJumpDelay() {
    }
}
