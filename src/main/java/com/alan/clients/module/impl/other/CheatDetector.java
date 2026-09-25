package com.alan.clients.module.impl.other;

import com.alan.clients.Client;
import com.alan.clients.anticheat.alert.AlertManager;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import lombok.Generated;
import net.minecraft.client.entity.EntityOtherPlayerMP;

@ModuleInfo(aliases = "module.other.cheatdetector.name", description = "module.other.cheatdetector.description", category = Category.PLAYER)
public final class CheatDetector extends Module {
    private final AlertManager alertManager = new AlertManager();
    public ModeValue alertType = new ModeValue("Alert Type", this).add(new SubMode("ClientSide")).add(new SubMode("ServerSide")).setDefault("ClientSide");
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> Client.a.n().incrementTick();
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var0 -> Client.a.n().playerMap.values().forEach(var1 -> {
        if (var1 != null) {
            try {
                var1.handle(var0.getPacket());
            } catch (NullPointerException nullpointerexception) {
            }
        }
    });

    public CheatDetector() {
    }

    @Override
    public void onDisable() {
        Client.a.n().playerMap.clear();
    }

    @Override
    public void onEnable() {
        if (aEg.theWorld != null) {
            Client.a.n().playerMap.clear();
            aEg.theWorld.playerEntities.forEach(var0 -> {
                if (var0 instanceof EntityOtherPlayerMP && var0 != aEg.thePlayer) {
                    Client.a.n().H().b((EntityOtherPlayerMP)var0);
                }
            });
        }
    }

    @Generated
    public AlertManager getAlertManager() {
        return this.alertManager;
    }
}
