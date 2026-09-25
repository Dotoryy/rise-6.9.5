package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import java.util.Iterator;
import net.minecraft.network.play.server.c;
import net.minecraft.util.IChatComponent;

@ModuleInfo(aliases = "module.other.hypixelautoplay.name", description = "module.other.hypixelautoplay.description", category = Category.PLAYER)
public final class HypixelAutoPlay extends Module {
    private final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Hypixel")).setDefault("Hypixel");
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (var1.getPacket() instanceof c c && this.mode.wo().getName().equals("Hypixel")) {
            if (c.isChat()) {
                return;
            }

            if (c.getChatComponent().getFormattedText().contains("play again?")) {
                Iterator iterator = c.getChatComponent().getSiblings().iterator();

                while (iterator.hasNext()) {
                    for (String s : ((IChatComponent)iterator.next()).toString().split("'")) {
                        if (s.startsWith("/play") && !s.contains(".")) {
                            ChatUtil.send(s);
                            NotificationComponent.a("Auto Play", "Joined a new game", 7000);
                            break;
                        }
                    }
                }
            }
        }
    };

    public HypixelAutoPlay() {
    }
}
