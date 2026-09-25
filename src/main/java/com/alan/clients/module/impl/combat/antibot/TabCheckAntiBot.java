package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.Mode;
import net.minecraft.entity.player.EntityPlayer;

public class TabCheckAntiBot extends Mode<AntiBot> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.theWorld != null && aEg.thePlayer != null) {
            for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
                if (entityplayer != aEg.thePlayer) {
                    if (aEg.getNetHandler().getPlayerInfo(entityplayer.getUniqueID()) == null) {
                        Client.a.getBotManager().b(this, entityplayer);
                    } else {
                        Client.a.getBotManager().c(this, entityplayer);
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1x -> Client.a.getBotManager().a(this);
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> Client.a.getBotManager().a(this);

    public TabCheckAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
