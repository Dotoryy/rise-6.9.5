package com.alan.clients.component.impl.performance;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import net.minecraft.network.play.server.S2APacketParticles;

public class ParticleDistanceComponent extends Component {
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        if (var0.getPacket() instanceof S2APacketParticles s2apacketparticles
            && aEg.thePlayer.getDistanceSq(s2apacketparticles.getXCoordinate(), s2apacketparticles.getYCoordinate(), s2apacketparticles.getZCoordinate())
                >= 36.0) {
            var0.setCancelled();
        }
    };

    public ParticleDistanceComponent() {
    }
}
