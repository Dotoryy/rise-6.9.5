package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.Mode;

public final class FuncraftCheckAntiBot extends Mode<AntiBot> {
    @EventLink
    private final Listener<PreUpdateEvent> onPreUpdate = var1x -> aEg.theWorld.playerEntities.forEach(var1xx -> {
        if (var1xx.getDisplayName().getUnformattedText().contains("§")) {
            Client.a.getBotManager().c(this, var1xx);
        } else {
            Client.a.getBotManager().b(this, var1xx);
        }
    });

    public FuncraftCheckAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
