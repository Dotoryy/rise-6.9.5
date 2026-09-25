package com.alan.clients.component.impl.hypixel;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.ServerUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.c;

public class APIKeyComponent
extends Component {
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    public static String apiKey;
    @EventLink
    public Listener<ServerJoinEvent> onServerJoin;
    public Pattern pattern = Pattern.compile("Your new API key is (.*)");
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public static boolean receivedKey;

    static {
    }

    public APIKeyComponent() {
        this.onPreMotion = preMotionEvent -> {
            if (!receivedKey && APIKeyComponent.aEg.thePlayer.ticksExisted == 2) {
                ServerUtil.vn();
            }
        };
        this.onPacketReceive = packetReceiveEvent -> {
            Packet<?> packet = packetReceiveEvent.getPacket();
            if (packet instanceof c) {
                if (!ServerUtil.vn()) {
                    return;
                }
                c c2 = (c)packet;
                String string = c2.getChatComponent().getUnformattedText();
                Matcher matcher = this.pattern.matcher(string);
                if (!c2.isChat() && matcher.find()) {
                    matcher.group(1);
                    if (!receivedKey) {
                        packetReceiveEvent.setCancelled();
                    }
                    receivedKey = true;
                }
            }
        };
        this.onServerJoin = serverJoinEvent -> {
            receivedKey = false;
        };
    }

}
