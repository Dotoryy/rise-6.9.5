package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.StringValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.c;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.s;

@ModuleInfo(aliases={"module.render.streamer.name"}, description="module.render.streamer.description", category=Category.RENDER)
public final class Streamer
extends Module {
    public final BooleanValue name = new BooleanValue("Name", (Module)this, (Boolean)true);
    public final StringValue replacement = new StringValue("Replacement", (Module)this, "You");
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = packetReceiveEvent -> {
        Packet<?> packet = packetReceiveEvent.getPacket();
        if (packet instanceof c && ((Boolean)this.name.wo()).booleanValue()) {
            c c2 = (c)packet;
            IChatComponent iChatComponent = c2.getChatComponent();
            if (iChatComponent instanceof s) {
                String string = iChatComponent.getFormattedText().replace(Streamer.aEg.thePlayer.getGameProfile().getName(), (CharSequence)this.replacement.wo());
                s s2 = new s(string);
                c2.l((IChatComponent)s2);
            }
            packetReceiveEvent.setPacket((Packet<?>)c2);
        }
    };
    @EventLink(value=4)
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        for (NetworkPlayerInfo networkPlayerInfo : aEg.getNetHandler().getPlayerInfoMap()) {
            if (networkPlayerInfo.getGameProfile().getName().length() < 3 || networkPlayerInfo.getDisplayName() == null) continue;
            networkPlayerInfo.setDisplayName((IChatComponent)new s(networkPlayerInfo.getDisplayName().getFormattedText().replaceFirst(Streamer.aEg.thePlayer.getGameProfile().getName(), (String)this.replacement.wo())));
        }
    };
}
