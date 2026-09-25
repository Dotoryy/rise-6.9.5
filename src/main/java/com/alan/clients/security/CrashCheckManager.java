package com.alan.clients.security;

import com.alan.clients.Client;
import com.alan.clients.module.impl.other.AntiCrash;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public final class CrashCheckManager extends ArrayList<CrashCheck> {
    private AntiCrash axd;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> var1.setCancelled(this.k(var1.getPacket()));

    public CrashCheckManager() {
    }

    public void init() {
        Client.a.e().b(this);
        this.axd = Client.a.g().c(AntiCrash.class);
        if (this.axd != null) {
            ;
        }
    }

    public boolean k(Packet<?> packet) {
        return this.axd != null && this.axd.isEnabled() && !Minecraft.getMinecraft().isSingleplayer() && this.stream().anyMatch(var1x -> var1x.handle(packet));
    }
}
