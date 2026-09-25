package com.alan.clients.module.impl.combat;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.SafeWalkEvent;

@ModuleInfo(aliases = "module.combat.fences.name", description = "module.combat.fences.description", category = Category.COMBAT)
public final class Fences extends Module {
    @EventLink
    public final Listener<SafeWalkEvent> lJ = var0 -> {
        aEg.thePlayer.crd = true;
        var0.h(-5.0);
    };

    public Fences() {
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.crd = false;
    }
}
