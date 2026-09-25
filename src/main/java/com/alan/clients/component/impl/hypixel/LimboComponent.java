package com.alan.clients.component.impl.hypixel;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.ServerKickEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import java.util.List;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.c;

public final class LimboComponent extends Component {
    private static final String cw = "An exception occurred in your connection, so you have been routed to limbo!";
    private int cx;
    private boolean cy;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (var1.getPacket() instanceof c) {
            String s = ((c)var1.getPacket()).getChatComponent().getUnformattedText();
            if (s != null && s.contains("An exception occurred in your connection, so you have been routed to limbo!")) {
                this.cy = true;
                this.cx = 100;
                return;
            }
        }

        if (this.cy && this.cx > 0 && (var1.getPacket() instanceof S01PacketJoinGame || var1.getPacket() instanceof S07PacketRespawn)) {
            ;
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        if (this.cx > 0) {
            this.cx--;
            if (this.cx == 0) {
                this.cy = false;
            }
        }
    };
    @EventLink
    public final Listener<ServerKickEvent> onServerKick = var1 -> {
        if (this.cy) {
            if (this.cx > 0) {
                List list = var1.getMessage();
                if (list != null && !list.isEmpty()) {
                    Client.a.getThemeManager().getTheme().getChatAccentColor();
                    this.cy = false;
                    this.cx = 0;
                }
            }
        }
    };

    public LimboComponent() {
    }

    @Override
    public void aT() {
        this.cx = 0;
        this.cy = false;
    }
}
