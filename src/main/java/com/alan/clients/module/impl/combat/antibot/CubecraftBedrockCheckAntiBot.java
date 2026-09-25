package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;

public final class CubecraftBedrockCheckAntiBot extends Mode<AntiBot> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> aEg.theWorld.playerEntities.forEach(var1xx -> {
        String s = var1xx.getDisplayName().getUnformattedText();
        stripFormatting(s);
        if (s.chars().filter(var0 -> var0 == 167).count() >= 8L && s.contains("§\u0000§\u0000§\u0000§\u0000")) {
            if (aEg.thePlayer.Zl == 10) {
                ChatUtil.c("Detected bot by formatting pattern: " + s);
            }

            Client.a.getBotManager().b(this, var1xx);
        }
    });

    public CubecraftBedrockCheckAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    public static String stripFormatting(String var0) {
        if (var0 == null) {
            return null;
        }

        String s = var0.replaceAll("§.", "");
        return s.replaceAll("\\p{Cntrl}", "").trim();
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
