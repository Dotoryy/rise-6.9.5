package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;

public final class ColourCheckAntiBot extends Mode<AntiBot> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> aEg.theWorld.playerEntities.forEach(var1xx -> {
        String s = var1xx.getDisplayName().getUnformattedText();
        if (!startsWithColorCode(s)) {
            ChatUtil.c("Detected bot (invalid colour start): " + s);
            Client.a.getBotManager().b(this, var1xx);
        }
    });

    public ColourCheckAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    public static boolean startsWithColorCode(String var0) {
        return var0 != null && !var0.isEmpty() ? var0.matches("^§[0-9a-fk-or].*") : false;
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
