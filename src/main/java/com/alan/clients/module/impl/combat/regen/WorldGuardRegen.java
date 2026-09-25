package com.alan.clients.module.impl.combat.regen;

import com.alan.clients.module.impl.combat.Regen;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

public final class WorldGuardRegen extends Mode<Regen> {
    private final NumberValue health = new NumberValue("Minimum Health", this, 15, 1, 20, 1);
    private int ticks;
    private float yaw;
    private float pitch;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (!aEg.thePlayer.onGround || !(aEg.thePlayer.getHealth() < this.health.wo().floatValue())) {
            this.yaw = var1x.getYaw();
            this.pitch = var1x.getPitch();
            this.ticks = 0;
        } else if (this.ticks <= 1) {
            var1x.setPosY(var1x.getPosY() - 0.05);
            var1x.setYaw(this.yaw);
            var1x.setPitch(this.pitch);
            this.ticks++;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        Packet packet = var1x.dq();
        if (packet instanceof C02PacketUseEntity && aEg.thePlayer.onGround && aEg.thePlayer.getHealth() < this.health.wo().floatValue() && this.ticks <= 1) {
            C02PacketUseEntity c02packetuseentity = (C02PacketUseEntity)packet;
            if (c02packetuseentity.getAction().equals(Action.ATTACK)) {
                var1x.setCancelled();
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, true));
                PacketUtil.sendNoEvent(c02packetuseentity);
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY - 0.05, aEg.thePlayer.posZ, false));
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if (var1x.getPacket() instanceof S2DPacketOpenWindow && this.ticks <= 1) {
            var1x.setCancelled();
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer.onGround && aEg.thePlayer.getHealth() < this.health.wo().floatValue() && this.ticks <= 1) {
            var1x.setSpeed(0.0);
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var1x -> {
        if (aEg.thePlayer.onGround && aEg.thePlayer.getHealth() < this.health.wo().floatValue() && this.ticks <= 1) {
            var1x.setJump(false);
        }
    };

    public WorldGuardRegen(String var1, Regen regen) {
        super(var1, regen);
    }
}
