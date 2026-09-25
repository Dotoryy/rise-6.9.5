package com.alan.clients.module.impl.movement.phase;

import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class WatchdogSemiBlockPhase extends Mode<Phase> {
    private final NumberValue amount = new NumberValue("Amount", this, 1, 0.1, 1.5, 0.1);
    private final NumberValue timer = new NumberValue("Timer", this, 0.1, 0.1, 1, 0.1);
    private final BooleanValue packet = new BooleanValue("Packet", this, false);
    private boolean canPhase;
    private boolean pushedThisTick;
    private double lastSetbackX;
    private double lastSetbackZ;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        this.pushedThisTick = false;
        double d0 = Math.toRadians(aEg.thePlayer.pl);
        double d1 = Math.sin(d0);
        double d2 = Math.cos(d0);
        if (aEg.thePlayer.isCollidedHorizontally) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX - d1 * 0.01, aEg.thePlayer.posY, aEg.thePlayer.posZ + d2 * 0.01);
            this.pushedThisTick = true;
        } else if (this.canPhase && PlayerUtil.vk()) {
            aEg.timer.dzD = this.timer.wo().floatValue();
            if (!this.packet.wo()) {
                aEg.thePlayer
                    .setPosition(aEg.thePlayer.posX - d1 * this.amount.wo().doubleValue(), aEg.thePlayer.posY, aEg.thePlayer.posZ + d2 * this.amount.wo().doubleValue());
            } else {
                PacketUtil.sendNoEvent(
                    new C04PacketPlayerPosition(
                        aEg.thePlayer.posX - d1 * this.amount.wo().doubleValue(), aEg.thePlayer.posY, aEg.thePlayer.posZ + d2 * this.amount.wo().doubleValue(), false
                    )
                );
            }

            this.pushedThisTick = true;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.canPhase && PlayerUtil.vk()) {
            MoveUtil.stop();
        }
    };
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = CancellableEvent::setCancelled;
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (var1x.getBlock() instanceof BlockAir && this.pushedThisTick) {
            double d2 = var1x.getBlockPos().getX();
            double d0 = var1x.getBlockPos().getY();
            d2 = var1x.getBlockPos().getZ();
            double d1;
            int i = (d1 = d0 - aEg.thePlayer.posY) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
        }
    };
    @EventLink
    private final Listener<MoveEvent> onMove = var1x -> {
        if (this.canPhase) {
            PlayerUtil.vk();
        }
    };
    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook s08packetplayerposlook = (S08PacketPlayerPosLook)var1x.getPacket();
            double d0 = s08packetplayerposlook.getX();
            double d1 = s08packetplayerposlook.getZ();
            double d2 = Math.abs(d0 - this.lastSetbackX);
            double d3 = Math.abs(d1 - this.lastSetbackZ);
            this.lastSetbackX = d0;
            this.lastSetbackZ = d1;
            if (d2 <= 0.001 && d3 <= 0.001) {
                this.canPhase = true;
            } else {
                this.canPhase = false;
            }
        }
    };

    public WatchdogSemiBlockPhase(String var1, Phase phase) {
        super(var1, phase);
    }

    @Override
    public void onEnable() {
        ChatUtil.b("try to jump around a bit before phasing if its the start of a game");
    }

    @Override
    public void onDisable() {
        this.canPhase = false;
    }
}
