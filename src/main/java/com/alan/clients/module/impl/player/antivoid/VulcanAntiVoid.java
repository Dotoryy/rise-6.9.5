package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;

public class VulcanAntiVoid extends Mode<AntiVoid> {
    private final NumberValue distance = new NumberValue("Distance", this, 2.6, 0, 10, 0.1);
    private boolean zd;
    private boolean voidFalling;
    private Flight flight = null;
    private Speed speed = null;
    private LongJump longJump = null;
    private boolean toggledSpeed = false;
    private int zn;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (this.flight == null) {
            this.flight = this.e(Flight.class);
        }

        if (this.speed == null) {
            this.speed = this.e(Speed.class);
        }

        if (this.longJump == null) {
            this.longJump = this.e(LongJump.class);
        }

        if (aEg.thePlayer.fallDistance > this.distance.wo().floatValue() && !PlayerUtil.vh()) {
            this.voidFalling = true;
        }

        if (this.flight.isEnabled() || this.longJump.isEnabled()) {
            this.voidFalling = false;
        }

        if (this.speed.isEnabled() && this.voidFalling) {
            this.toggledSpeed = true;
            this.speed.toggle();
        }

        if (!this.voidFalling && !this.speed.isEnabled() && this.toggledSpeed) {
            this.speed.toggle();
            this.toggledSpeed = false;
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (var1x.getBlock() instanceof BlockAir && !aEg.thePlayer.isSneaking() && this.voidFalling) {
            double d0 = var1x.getBlockPos().getX();
            double d1 = var1x.getBlockPos().getY();
            double d2 = var1x.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var1x.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }

        if ((!(var1x.getBlock() instanceof BlockAir) || aEg.thePlayer.isSneaking()) && this.voidFalling && !aEg.thePlayer.isCollidedHorizontally) {
            this.voidFalling = false;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.voidFalling) {
            MoveUtil.strafe(0.1);
            if (aEg.thePlayer.ticksExisted % 2 != 1 && aEg.thePlayer.moveForward == 0.0F) {
                MoveUtil.strafe(0.0);
                var1x.setForward(-1.0F);
            } else {
                var1x.setForward(1.0F);
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            this.voidFalling = false;
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1x -> this.voidFalling = false;
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> {
        if (this.voidFalling) {
            var1x.setJumpMotion(0.0F);
        }
    };

    public VulcanAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
