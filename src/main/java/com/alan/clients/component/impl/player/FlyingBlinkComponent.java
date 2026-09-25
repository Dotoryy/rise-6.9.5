package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FlyingBlinkComponent extends Component {
    public static final ConcurrentLinkedQueue<Packet<?>> dA = new ConcurrentLinkedQueue<>();
    public static boolean cR;
    @EventLink(value = 1)
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        if (aEg.thePlayer == null) {
            cR = false;
            dA.clear();
        } else if (aEg.thePlayer.isDead || aEg.isSingleplayer() || PacketQueueComponent.cR) {
            dA.forEach(PacketUtil::sendNoEvent);
            dA.clear();
            cR = false;
        } else if (!var0.isCancelled()) {
            Packet packet = var0.dq();
            if (cR) {
                if (packet instanceof C03PacketPlayer) {
                    dA.add(packet);
                    var0.setCancelled();
                }
            } else if (packet instanceof C03PacketPlayer) {
                dA.forEach(PacketUtil::sendNoEvent);
                dA.clear();
            }
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var0 -> {
        cR = false;
        dA.clear();
    };

    public FlyingBlinkComponent() {
    }

    public static void bc() {
        dA.forEach(PacketUtil::sendNoEvent);
        dA.clear();
        cR = false;
    }
}
