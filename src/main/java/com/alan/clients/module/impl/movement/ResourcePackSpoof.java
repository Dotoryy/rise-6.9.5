package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C19PacketResourcePackStatus.Action;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;

@ModuleInfo(aliases = "Resource Pack Spoof", description = "Allows you to pretend you loaded a resource pack a server requested", category = Category.MOVEMENT)
public final class ResourcePackSpoof extends Module {
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        if (var0.getPacket() instanceof S48PacketResourcePackSend s48packetresourcepacksend) {
            var0.setCancelled();
            PacketUtil.send(new C19PacketResourcePackStatus(s48packetresourcepacksend.getHash(), Action.SUCCESSFULLY_LOADED));
            ChatUtil.b("Spoofed resource pack from " + s48packetresourcepacksend.getURL());
        }
    };

    public ResourcePackSpoof() {
    }
}
