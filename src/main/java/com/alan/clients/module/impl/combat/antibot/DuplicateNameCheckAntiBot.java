package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;

public final class DuplicateNameCheckAntiBot extends Mode<AntiBot> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> aEg.theWorld.playerEntities.forEach(var1xx -> {
        String s = var1xx.getDisplayName().getUnformattedText();
        if (aEg.theWorld.playerEntities.stream().anyMatch(var1xxx -> s.equals(var1xxx.getDisplayName().getUnformattedText()))) {
            Client.a.getBotManager().b(this, var1xx);
        }
    });

    public DuplicateNameCheckAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
