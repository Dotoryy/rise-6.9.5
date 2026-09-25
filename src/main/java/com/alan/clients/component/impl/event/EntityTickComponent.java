package com.alan.clients.component.impl.event;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class EntityTickComponent extends Component {
    @EventLink(value = 0)
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        if (aEg != null && aEg.theWorld != null && !var0.isCancelled()) {
            Packet packet = var0.dq();
            if (packet instanceof C08PacketPlayerBlockPlacement && !((C08PacketPlayerBlockPlacement)packet).getPosition().h(new Vector3d(-1.0, -1.0, -1.0))) {
                aEg.thePlayer.crH = 0;
            }
        }
    };
    @EventLink(value = 0)
    public final Listener<AttackEvent> onAttack = var0 -> aEg.thePlayer.aY = 0;
    @EventLink(value = 0)
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        if (aEg != null && aEg.theWorld != null && !var0.isCancelled()) {
            Packet packet = var0.getPacket();
            if (packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
                Entity entity = aEg.theWorld.getEntityByID(s12packetentityvelocity.getEntityID());
                if (entity == null) {
                    return;
                }

                entity.crI = s12packetentityvelocity.motionX / 8000.0;
                entity.crJ = s12packetentityvelocity.motionY / 8000.0;
                entity.crK = s12packetentityvelocity.motionZ / 8000.0;
                entity.ae = 0;
                if (s12packetentityvelocity.motionY / 8000.0 > 0.1
                    && Math.hypot(s12packetentityvelocity.motionZ / 8000.0, s12packetentityvelocity.motionX / 8000.0) > 0.2) {
                    entity.crG = 0;
                }
            } else if (packet instanceof S08PacketPlayerPosLook) {
                aEg.thePlayer.Zl = 0;
            }
        }
    };

    public EntityTickComponent() {
    }
}
