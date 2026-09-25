package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
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
import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Grim2Flight extends Mode<Flight> {
    private boolean pendingFallFlying = false;
    private boolean blinking = false;
    private int pendingTicks = 0;
    public static final Queue<Packet<?>> heldPackets = new LinkedList<>();
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1x -> {
        this.getParent().toggle();
        this.pendingTicks = 0;
        this.pendingFallFlying = false;
        this.blinking = false;
        heldPackets.clear();
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.pendingFallFlying) {
            this.pendingTicks++;
            if (this.pendingTicks >= 8) {
                for (int i = 0; i < 1; i++) {
                    this.sendFallFlyingPacket();
                }
            }

            this.pendingTicks = 0;
            this.pendingFallFlying = false;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        Packet packet = var1x.dq();
        if (packet instanceof C0FPacketConfirmTransaction && this.blinking) {
            var1x.setCancelled(true);
            if (heldPackets.isEmpty()) {
                this.pendingFallFlying = true;
            }

            heldPackets.add(packet);
        }

        if (packet instanceof C02PacketUseEntity && this.blinking && !heldPackets.isEmpty()) {
            while (!heldPackets.isEmpty()) {
                PacketUtil.sendNoEvent(heldPackets.poll());
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S08PacketPlayerPosLook && this.blinking && !heldPackets.isEmpty()) {
            while (!heldPackets.isEmpty()) {
                PacketUtil.sendNoEvent(heldPackets.poll());
            }
        }

        if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == aEg.thePlayer.getEntityId()) {
            if (this.blinking || !heldPackets.isEmpty()) {
                return;
            }

            this.pendingTicks = 0;
            this.blinking = true;
            var1x.setCancelled(true);
        }
    };

    public Grim2Flight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.pendingTicks = 0;
        this.pendingFallFlying = false;
        this.blinking = false;
        heldPackets.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.pendingTicks = 0;
        this.pendingFallFlying = false;
        this.blinking = false;

        while (!heldPackets.isEmpty()) {
            PacketUtil.sendNoEvent(heldPackets.poll());
        }

        super.onDisable();
    }

    private void sendFallFlyingPacket() {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.PLAYER_COMMAND, userconnection);
            packetwrapper.write(Types.VAR_INT, aEg.thePlayer.getEntityId());
            packetwrapper.write(Types.VAR_INT, 8);
            packetwrapper.write(Types.VAR_INT, 0);
            packetwrapper.sendToServer(Protocol1_9To1_8.class);
        }
    }
}
