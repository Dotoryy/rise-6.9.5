package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Grim2Speed
extends Mode<Speed> {
    public boolean shouldJump;
    public int groundTicks;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    public int strafeTicks;
    @EventLink
    public Listener<TeleportEvent> onTeleport;
    public boolean Ga;
    public BooleanValue highPingModeMa = new BooleanValue("High Ping Mode (May be slower)", (Mode<?>)this, (Boolean)false);
    public NumberValue speed = new NumberValue("Speed", this, (Number)1, (Number)0, (Number)1, (Number)0.001);
    @EventLink
    public Listener<JumpEvent> onJump = jumpEvent -> {
        if (!Grim2Speed.aEg.thePlayer.isJumping) {
            jumpEvent.setCancelled();
        }
    };
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    @EventLink
    public Listener<PostMotionEvent> onPostMotion;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    @EventLink
    public Listener<StrafeEvent> onStrafe = strafeEvent -> {
        double d2 = 0.0;
        this.groundTicks = Grim2Speed.aEg.thePlayer.onGround ? ++this.groundTicks : 0;
        if (this.groundTicks >= 1) {
            boolean unused0 = Grim2Speed.aEg.thePlayer.onGround;
        }
        if (this.strafeTicks > -1) {
            double d3 = 0.03;
            if (this.strafeTicks % 2 == 0) {
                d3 = Grim2Speed.aEg.thePlayer.onGround ? 0.085 : 0.03;
            }
            MoveUtil.moveFlying(d3 * ((Number)this.speed.wo()).doubleValue());
        }
        ++this.strafeTicks;
    };
    public boolean sentDoublePacket;
    @EventLink
    public Listener<PostStrafeEvent> onPostStrafe = postStrafeEvent -> {
        this.sentDoublePacket = this.sentDoublePacket;
    };

    static {
    }


    public Grim2Speed(String string, Speed speed) {
        super(string, speed);
        this.onPreMotion = preMotionEvent -> {
            this.shouldJump = false;
        };
        this.onPostMotion = postMotionEvent -> {
            if (this.strafeTicks % 2 == 0) {
                if (!((Boolean)this.highPingModeMa.wo()).booleanValue()) {
                    PacketUtil.send(new C03PacketPlayer(true));
                    PacketUtil.send(new C03PacketPlayer(false));
                } else {
                    PacketUtil.send(new C03PacketPlayer(false));
                    PacketUtil.send(new C03PacketPlayer(false));
                }
                this.sentDoublePacket = true;
            }
        };
        this.onMoveInput = moveInputEvent -> {
            if (this.shouldJump) {
                moveInputEvent.setJump(true);
            }
        };
        this.onPacketReceive = packetReceiveEvent -> {
            Packet<?> packet = packetReceiveEvent.getPacket();
            if (packet instanceof S08PacketPlayerPosLook) {
                if (this.strafeTicks % 2 == 1) {
                    ++this.strafeTicks;
                }
                Grim2Speed.aEg.timer.dzD = 1.0f;
            }
            if (packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)packet;
                this.sentDoublePacket = false;
                if (s12PacketEntityVelocity.getEntityID() == Grim2Speed.aEg.thePlayer.getEntityId()) {
                    this.shouldJump = true;
                }
            }
        };
        this.onTeleport = teleportEvent -> {
            Grim2Speed.aEg.timer.dzD = 1.0f;
        };
    }

    @Override
    public void onDisable() {
        Grim2Speed.aEg.timer.dzD = 1.0f;
    }

    @Override
    public void onEnable() {
        if (!((Boolean)this.highPingModeMa.wo()).booleanValue()) {
            PacketUtil.send(new C03PacketPlayer(true));
            PacketUtil.send(new C03PacketPlayer(false));
        } else {
            PacketUtil.send(new C03PacketPlayer(false));
            PacketUtil.send(new C03PacketPlayer(false));
        }
        if (ViaLoadingBase.getInstance().getTargetVersion().equalTo(ProtocolVersion.v1_8) || ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_18_2)) {
            ChatUtil.b("this will only work 1.9-1.18.1", new Object[0]);
        } else {
            ChatUtil.b("ping needs to be below 150ms for this to work consistantly", new Object[0]);
        }
        this.strafeTicks = 0;
        this.groundTicks = 0;
        Grim2Speed.aEg.timer.dzD = 1.0f;
    }
}
