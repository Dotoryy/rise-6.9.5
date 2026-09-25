package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.BooleanValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.ay;

@ModuleInfo(aliases = "module.other.noguiclose.name", category = Category.PLAYER, description = "module.other.noguiclose.description")
public class NoGuiClose extends Module {
    public BooleanValue chatonly = new BooleanValue("Chat Only", this, false);
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        var1.getPacket();
        if (var1.getPacket() instanceof ay && (aEg.currentScreen instanceof GuiChat || !this.chatonly.wo())) {
            var1.setCancelled();
        }
    };

    static {
    }

    public NoGuiClose() {
    }

}
