package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.a;
import net.minecraft.util.MathHelper;

public class WatchdogPredictionFlight
extends Mode<Flight> {
    @EventLink
    public Listener<TeleportEvent> onTeleport = teleportEvent -> {
        this.boosting = this.boosting;
    };
    @EventLink
    public Listener<PostStrafeEvent> onPostStrafe = postStrafeEvent -> {
        int unused0 = WatchdogPredictionFlight.aEg.thePlayer.Zl;
        boolean unused1 = WatchdogPredictionFlight.aEg.thePlayer.onGround;
        if (WatchdogPredictionFlight.aEg.thePlayer.tR == 5) {
            this.boosting = true;
        }
        if (!WatchdogPredictionFlight.aEg.thePlayer.onGround && WatchdogPredictionFlight.aEg.thePlayer.Zl != 1) {
            WatchdogPredictionFlight.aEg.thePlayer.cameraYaw = 0.1f;
        }
        if (WatchdogPredictionFlight.aEg.thePlayer.motionY < 0.0) {
            int unused2 = WatchdogPredictionFlight.aEg.thePlayer.tR;
        }
    };
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    public boolean boosting;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive = packetReceiveEvent -> {
        Packet<?> packet;
        Packet<?> packet2;
        Object var30_2 = null;
        Object var31_3 = null;
        Packet<?> packet3 = packetReceiveEvent.getPacket();
        if (packet3 instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)packet3;
            s12PacketEntityVelocity.getEntityID();
            WatchdogPredictionFlight.aEg.thePlayer.getEntityId();
        }
        if ((packet2 = packetReceiveEvent.getPacket()) instanceof a) {
            a a2 = (a)packet2;
        }
        if ((packet = packetReceiveEvent.getPacket()) instanceof S08PacketPlayerPosLook && this.boosting) {
            S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)packet;
            s08PacketPlayerPosLook.getX();
            s08PacketPlayerPosLook.getY();
            s08PacketPlayerPosLook.getZ();
            s08PacketPlayerPosLook.getYaw();
            s08PacketPlayerPosLook.getPitch();
        }
    };
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    @EventLink
    public Listener<TeleportEvent> onTeleportMedium;


    public WatchdogPredictionFlight(String string, Flight flight) {
        super(string, flight);
        this.onPreMotion = preMotionEvent -> {
            if (WatchdogPredictionFlight.aEg.thePlayer.ticksExisted % 2 == 0 && this.boosting) {
                Double direction = MoveUtil.direction();
                double unused0 = -MathHelper.sin((float)((float)direction.doubleValue())) * 100.0f;
                double unused1 = MathHelper.cos((float)((float)direction.doubleValue())) * 100.0f;
                preMotionEvent.setPosY(preMotionEvent.getPosY() + 100.0);
                WorldClient unused2 = Minecraft.getMinecraft().theWorld;
                int gZ = Minecraft.getMinecraft().theWorld.GZ();
            }
            WorldClient worldClient = Minecraft.getMinecraft().theWorld;
            int gZ2 = Minecraft.getMinecraft().theWorld.GZ();
        };
        this.onPacketSend = packetSendEvent -> {
            Packet<?> packet = packetSendEvent.dq();
            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                C03PacketPlayer.C04PacketPlayerPosition unused0 = (C03PacketPlayer.C04PacketPlayerPosition)packet;
            }
        };
        this.onTeleportMedium = teleportEvent -> {};
    }

    @Override
    public void onDisable() {
        this.boosting = false;
        WatchdogPredictionFlight.aEg.thePlayer.capabilities.isFlying = false;
    }

    static {
    }

    @Override
    public void onEnable() {
        ChatUtil.c(WatchdogPredictionFlight.aEg.thePlayer.tR, new Object[0]);
        if (WatchdogPredictionFlight.aEg.thePlayer.onGround) {
            WatchdogPredictionFlight.aEg.thePlayer.jump();
        }
        this.boosting = false;
    }
}
