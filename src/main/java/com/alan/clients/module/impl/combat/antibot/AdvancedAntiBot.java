package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.Mode;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class AdvancedAntiBot extends Mode<AntiBot> {
    private boolean tabListCaptured;
    private final Set<UUID> tabListUuids = new HashSet<>();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.theWorld != null && aEg.thePlayer != null) {
            EntityLivingBase entitylivingbase = this.e(KillAura.class).jE;
            if (!this.tabListCaptured && aEg.thePlayer.ticksExisted >= 150 && this.e(KillAura.class).isEnabled() && entitylivingbase != null && !entitylivingbase.isDead) {
                this.tabListUuids.clear();

                for (NetworkPlayerInfo networkplayerinfo : aEg.getNetHandler().getPlayerInfoMap()) {
                    if (networkplayerinfo != null && networkplayerinfo.getGameProfile() != null) {
                        this.tabListUuids.add(networkplayerinfo.getGameProfile().getId());
                    }
                }

                this.tabListCaptured = true;
            }

            for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
                if (entityplayer != aEg.thePlayer) {
                    if (!this.tabListCaptured) {
                        Client.a.getBotManager().c(this, entityplayer);
                    } else {
                        UUID uuid = entityplayer.getUniqueID();
                        if (this.tabListUuids.contains(uuid)) {
                            Client.a.getBotManager().c(this, entityplayer);
                        } else {
                            Client.a.getBotManager().b(this, entityplayer);
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1x -> {
        this.tabListCaptured = false;
        this.tabListUuids.clear();
        Client.a.getBotManager().a(this);
    };

    public AdvancedAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    @Override
    public void onDisable() {
        this.tabListCaptured = false;
        this.tabListUuids.clear();
        Client.a.getBotManager().a(this);
    }
}
