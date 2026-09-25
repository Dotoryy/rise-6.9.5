package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class GrimLongJump
extends Mode<LongJump> {
    public int strafeTicks;
    public int cancelNextPacket;
    public int hV;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public int timeoutTicks;
    public int started;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion = preMotionEvent -> {
        GrimLongJump.aEg.timer.dzD = 2.0f;
        preMotionEvent.setPitch((float)((double)preMotionEvent.getPitch() + (double)((float)Math.random()) * 0.1));
        if (this.started == 0) {
            return;
        }
        GrimLongJump.aEg.thePlayer.motionY = 0.42f;
        if (this.stage == 1) {
            this.stage = 2;
            this.timeoutTicks = 0;
            return;
        }
        if (this.stage != 2) {
            GrimLongJump.aEg.thePlayer.motionY = 0.0;
            GrimLongJump.aEg.thePlayer.motionX = 0.0;
            GrimLongJump.aEg.thePlayer.motionZ = 0.0;
            this.cancelNextPacket = 1;
            ++this.timeoutTicks;
            if (this.timeoutTicks <= 20) return;
            this.toggle();
            return;
        }
        int l5_hi = 0;
        while (true) {
            if (l5_hi >= 2) {
                this.cancelNextPacket = 1;
                this.stage = 0xFFFFFFC0 ^ 0xFFFFFFC0;
                return;
            }
            PacketUtil.sendNoEvent(new C03PacketPlayer(false));
            l5_hi++;
        }
    };
    public int stage;
    public double Lg;
    public int Lk;
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    @EventLink
    public Listener<PostStrafeEvent> onPostStrafe = postStrafeEvent -> {
        if (this.strafeTicks == 0) {
            GrimLongJump.aEg.thePlayer.jump();
        }
        if (this.strafeTicks == 1) {
            this.started = 1;
            int i = 0;
            while (i < 20) {
                PacketUtil.sendNoEvent(new C03PacketPlayer(false));
                i++;
            }
        }
        ++this.strafeTicks;
    };


    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        this.strafeTicks = 0;
        this.started = 0;
        this.stage = 0;
        this.Lk = 0;
        this.cancelNextPacket = 0;
        this.timeoutTicks = 0;
    }

    static {
    }

    public GrimLongJump(String string, LongJump longJump) {
        super(string, longJump);
        this.onPacketReceive = packetReceiveEvent -> {
            S12PacketEntityVelocity s12PacketEntityVelocity;
            Packet<?> packet = packetReceiveEvent.getPacket();
            if (packet instanceof S12PacketEntityVelocity && (s12PacketEntityVelocity = (S12PacketEntityVelocity)packet).getEntityID() == GrimLongJump.aEg.thePlayer.getEntityId() && (double)s12PacketEntityVelocity.getMotionY() / 8000.0 < 0.0) {
                this.toggle();
            }
            if (packetReceiveEvent.getPacket() instanceof S08PacketPlayerPosLook) {
                this.stage = 1;
            }
        };
        this.onPacketSend = packetSendEvent -> {
            if (packetSendEvent.dq() instanceof C03PacketPlayer && this.cancelNextPacket == 1) {
                this.cancelNextPacket = 0;
                packetSendEvent.setCancelled();
            }
        };
    }
}
