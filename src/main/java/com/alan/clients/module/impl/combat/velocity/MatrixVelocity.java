package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class MatrixVelocity
extends Mode<Velocity> {
    @EventLink
    public Listener<StrafeEvent> onStrafe;
    @EventLink
    public Listener<TeleportEvent> onTeleport;
    @EventLink(value=0)
    public Listener<PacketReceiveEvent> onPacketReceive = packetReceiveEvent -> {
        Object var11_2 = null;
        Object var12_3 = null;
        Packet<?> packet = packetReceiveEvent.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)packet;
            if (s12PacketEntityVelocity.getEntityID() == MatrixVelocity.aEg.thePlayer.getEntityId()) {
                MatrixVelocity.aEg.thePlayer.motionY = (double)s12PacketEntityVelocity.getMotionY() / 8000.0;
                if (!this.e(Speed.class).isEnabled() && MoveUtil.isMoving()) {
                    packetReceiveEvent.setCancelled();
                }
                return;
            }
            packetReceiveEvent.setPacket((Packet<?>)s12PacketEntityVelocity);
        }
    };

    static {
    }


    public MatrixVelocity(String string, Velocity velocity) {
        super(string, velocity);
        this.onStrafe = strafeEvent -> {
            if (!MoveUtil.isMoving() && MatrixVelocity.aEg.thePlayer.ae == 1) {
                MatrixVelocity.aEg.thePlayer.motionX *= -0.1;
                MatrixVelocity.aEg.thePlayer.motionZ *= -0.1;
            } else if (MatrixVelocity.aEg.thePlayer.ae == 1 && !this.e(Speed.class).isEnabled()) {
                MoveUtil.strafe();
            }
            if (MatrixVelocity.aEg.thePlayer.ae < 6 && this.e(Speed.class).isEnabled()) {
                MoveUtil.strafe();
            } else if (MatrixVelocity.aEg.thePlayer.ae > 1 && !this.e(Speed.class).isEnabled() && MatrixVelocity.aEg.thePlayer.ae < 15 && !this.e(Flight.class).isEnabled()) {
                MatrixVelocity.aEg.thePlayer.motionY -= 0.00348;
            }
            if (MatrixVelocity.aEg.thePlayer.ae < 10 && MatrixVelocity.aEg.thePlayer.ae > 1) {
                this.e(Flight.class).isEnabled();
            }
        };
        this.onTeleport = teleportEvent -> {
            double d2 = teleportEvent.getPosY() - (MatrixVelocity.aEg.thePlayer.posY - 2.0);
        };
    }
}
