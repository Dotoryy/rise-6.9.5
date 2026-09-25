package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.a;
import net.minecraft.util.MathHelper;

public class WatchdogPredictionFullBlockPhase extends Mode<Phase> {
    private boolean waitingForSetback;
    @EventLink
    private Listener<TeleportEvent> onTeleport = var1x -> this.waitingForSetback = this.waitingForSetback;
    @EventLink
    private Listener<PostStrafeEvent> onPostStrafe = var1x -> {
        MoveUtil.stop();
        if (aEg.thePlayer.tR == 5) {
            this.waitingForSetback = true;
        }

        if (!aEg.thePlayer.onGround && aEg.thePlayer.Zl != 1) {
            aEg.thePlayer.cameraYaw = 0.1F;
        }

        if (aEg.thePlayer.motionY < 0.0) {
            ;
        }

        aEg.thePlayer.motionY = 0.0;
    };
    @EventLink
    private Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity) {
            s12packetentityvelocity.getEntityID();
            aEg.thePlayer.getEntityId();
        }

        if (var1x.getPacket() instanceof a a) {
            ;
        }

        Packet packet = var1x.getPacket();
        if (packet instanceof S08PacketPlayerPosLook && this.waitingForSetback) {
            S08PacketPlayerPosLook s08packetplayerposlook = (S08PacketPlayerPosLook)packet;
            s08packetplayerposlook.getX();
            s08packetplayerposlook.getY();
            s08packetplayerposlook.getZ();
            s08packetplayerposlook.getYaw();
            s08packetplayerposlook.getPitch();
        }
    };
    @EventLink
    private Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.thePlayer.ticksExisted % 2 == 0) {
            double d0 = MoveUtil.direction();
            double d1 = -MathHelper.sin((float)d0) * 100.0F;
            d1 = MathHelper.cos((float)d0) * 100.0F;
            var0.setPosY(var0.getPosY() + 100.0);
            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            Minecraft.getMinecraft().theWorld.GZ();
            PacketWrapper.create(ServerboundPackets1_9.ACCEPT_TELEPORTATION, userconnection).write(Types.VAR_INT, 12);
        }

        UserConnection userconnection1 = Via.getManager().getConnectionManager().getConnections().iterator().next();
        Minecraft.getMinecraft().theWorld.GZ();
        PacketWrapper.create(ServerboundPackets1_9.ACCEPT_TELEPORTATION, userconnection1).write(Types.VAR_INT, 10);
    };
    @EventLink
    private Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (packet instanceof C04PacketPlayerPosition) {
            ;
        }
    };
    @EventLink
    private final Listener<TeleportEvent> onTeleportMedium = var0 -> {};

    public WatchdogPredictionFullBlockPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    @Override
    public void onEnable() {
        ChatUtil.c("collide with a block and wait until the server clips you down");
        this.waitingForSetback = false;
    }

    @Override
    public void onDisable() {
        this.waitingForSetback = false;
        aEg.thePlayer.capabilities.isFlying = false;
    }
}
