package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class MatrixFlight extends Mode<Flight> {
    private boolean Hs;
    private boolean Ht;
    private boolean teleported;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if (var1x.getPacket() instanceof S08PacketPlayerPosLook s08packetplayerposlook) {
            var1x.setCancelled(true);
            double d0 = s08packetplayerposlook.getX();
            double d1 = s08packetplayerposlook.getY();
            double d2 = s08packetplayerposlook.getZ();
            float f = aEg.thePlayer.pl;
            float f1 = aEg.thePlayer.rotationPitch;
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
                UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
                PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.ACCEPT_TELEPORTATION, userconnection);
                packetwrapper.write(Types.VAR_INT, s08packetplayerposlook.field_180058_f);
                packetwrapper.sendToServer(Protocol1_9To1_8.class);
            }

            PacketUtil.sendNoEvent(new C06PacketPlayerPosLook(d0, d1, d2, f, f1, false));
            aEg.thePlayer.setPosition(d0, d1, d2);
            aEg.thePlayer.jump();
            if (this.teleported) {
                this.getParent().setEnabled(false);
            }

            this.teleported = true;
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (aEg.thePlayer.tR > 15 && aEg.thePlayer.tR < 19) {
            var0.setForward(0.0F);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.ae > 1) {
            aEg.thePlayer.motionY += 0.00348;
        }

        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }

        if (aEg.thePlayer.fallDistance > 0.1 && !this.teleported) {
            aEg.thePlayer.motionY = 0.42;
            MoveUtil.strafe(1.97);
        }

        if (this.teleported && aEg.thePlayer.tR == 20) {
            aEg.thePlayer.motionY = 0.42;
            MoveUtil.strafe(9.3);
        }
    };

    public MatrixFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.teleported = false;
        this.Hs = false;
    }

    @Override
    public void onDisable() {
        this.teleported = false;
        this.Hs = false;
    }
}
