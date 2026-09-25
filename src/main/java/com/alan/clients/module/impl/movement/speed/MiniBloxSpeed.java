package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSnow;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public final class MiniBloxSpeed extends Mode<Speed> {
    private final NumberValue speed = new NumberValue("Speed", this, 4, 1, 9.5, 0.1);
    private double moveSpeed;
    private boolean payloadSent;
    private static final int SEARCH_RADIUS = 500;
    private int payloadTick;
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = CancellableEvent::setCancelled;
    @EventLink
    private final Listener<TeleportEvent> onTeleport = var1x -> {
        if (aEg.thePlayer.ticksExisted - this.payloadTick > 7) {
            ChatUtil.c("silently accepted");
            var1x.setCancelled();
        }

        this.moveSpeed = this.getBaseMoveSpeed();
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        MoveUtil.useDiagonalSpeed();
        if (aEg.thePlayer.onGround && MoveUtil.isMoving()) {
            aEg.thePlayer.jump();
            aEg.thePlayer.motionY = 0.399;
        }

        if (this.payloadSent && aEg.thePlayer.Zl > 0) {
            MoveUtil.strafe(this.getBaseMoveSpeed() * this.speed.wo().floatValue());
        } else if (aEg.thePlayer.Zl > 0) {
            MoveUtil.strafe(this.getBaseMoveSpeed() * 1.24);
        }
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> BlinkComponent.a(25000, true, false, false, false, false, false);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.payloadSent && aEg.thePlayer.ticksExisted - this.payloadTick >= 24) {
            this.payloadSent = false;
        }

        double d0 = Math.toRadians(aEg.thePlayer.pl);
        double d1 = Math.sin(d0);
        double d2 = Math.cos(d0);
        if (aEg.thePlayer.isCollidedHorizontally) {
            double d3 = aEg.thePlayer.posX - d1 * 0.005;
            double d7 = aEg.thePlayer.posZ + d2 * 0.005;
        } else if (PlayerUtil.vk()) {
        }

        if (aEg.thePlayer.ticksExisted % 24 == 0) {
            ChatUtil.c("payload sent");
            BlockPos blockpos = this.findNearestSolidBlock(500);
            if (blockpos != null) {
                this.payloadSent = true;
                this.payloadTick = aEg.thePlayer.ticksExisted;
                double d4 = blockpos.getX() + 0;
                double d5 = blockpos.getY() + 0;
                double d6 = blockpos.getZ() + 0;
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(d4 + 0.5, d5, d6 + 0.5, aEg.thePlayer.onGround));
                PacketUtil.sendNoEvent(
                    new C04PacketPlayerPosition(
                        aEg.thePlayer.posX + aEg.thePlayer.motionX,
                        aEg.thePlayer.posY + aEg.thePlayer.motionY,
                        aEg.thePlayer.posZ + aEg.thePlayer.motionZ,
                        aEg.thePlayer.onGround
                    )
                );
            } else {
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(d1 * 9.5, aEg.thePlayer.posY, d2 * 9.5, aEg.thePlayer.onGround));
            }
        }
    };

    public MiniBloxSpeed(String var1, Speed var2) {
        super(var1, var2);
    }

    @Override
    public void onEnable() {
        BlockPos blockpos = this.findNearestSolidBlock(500);
        if (blockpos != null) {
            this.payloadSent = true;
            this.payloadTick = aEg.thePlayer.ticksExisted;
            double d0 = blockpos.getX() + 0;
            double d1 = blockpos.getY() + 0;
            double d2 = blockpos.getZ() + 0;
            PacketUtil.sendNoEvent(new C04PacketPlayerPosition(d0 + 0.5, d1, d2 + 0.5, aEg.thePlayer.onGround));
            PacketUtil.sendNoEvent(
                new C04PacketPlayerPosition(
                    aEg.thePlayer.posX + aEg.thePlayer.motionX,
                    aEg.thePlayer.posY + aEg.thePlayer.motionY,
                    aEg.thePlayer.posZ + aEg.thePlayer.motionZ,
                    aEg.thePlayer.onGround
                )
            );
        }

        this.moveSpeed = 0.0;
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }

    private BlockPos findNearestSolidBlock(int var1) {
        double d0 = aEg.thePlayer.posX;
        double d1 = aEg.thePlayer.posY;
        double d2 = aEg.thePlayer.posZ;
        int i = MathHelper.floor_double(d1);
        double d3 = Double.MAX_VALUE;
        BlockPos blockpos = null;

        for (int j = -var1; j <= var1; j++) {
            for (int k = -var1; k <= var1; k++) {
                int l = MathHelper.floor_double(d0) + j;
                int i1 = MathHelper.floor_double(d2) + k;
                BlockPos blockpos1 = new BlockPos(l, i, i1);
                Block block = aEg.theWorld.getBlockState(blockpos1).getBlock();
                if (block.getMaterial().isSolid() && block.isFullBlock() && !(block instanceof BlockLeaves) && !(block instanceof BlockSnow)) {
                    double d4 = aEg.thePlayer.getDistance(blockpos1.getX() + 0.5, blockpos1.getY(), blockpos1.getZ() + 0.5);
                    if (d4 < d3) {
                        d3 = d4;
                        blockpos = blockpos1;
                    }
                }
            }
        }

        return blockpos;
    }

    private double getBaseMoveSpeed() {
        return this.applySpeedEffect(0.2873);
    }

    private double applySpeedEffect(double var1) {
        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            var1 *= 1.0 + 0.2 * (aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return var1;
    }
}
