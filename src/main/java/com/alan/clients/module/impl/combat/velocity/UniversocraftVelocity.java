package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class UniversocraftVelocity extends Mode<Velocity> {
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if ((!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) && !var1x.isCancelled()) {
            Packet packet = var1x.getPacket();
            if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == aEg.thePlayer.getEntityId()) {
                var1x.setCancelled();
                aEg.thePlayer.motionY = aEg.thePlayer.motionY + (0.1 - Math.random() / 100.0);
            }

            if (packet instanceof S27PacketExplosion) {
                var1x.setCancelled();
                aEg.thePlayer.motionY = aEg.thePlayer.motionY + (0.1 - Math.random() / 100.0);
            }
        }
    };

    public UniversocraftVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
