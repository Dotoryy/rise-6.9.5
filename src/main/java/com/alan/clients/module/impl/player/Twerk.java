package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;

@ModuleInfo(aliases = "module.player.twerk.name", description = "module.player.twerk.description", category = Category.PLAYER)
public class Twerk extends Module {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> aEg.gameSettings.keyBindSneak.setPressed(Math.random() < 0.5);

    public Twerk() {
    }
}
