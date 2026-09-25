package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;

public class MineMenClubSpeed extends Mode<Speed> {
    private static float strafeAngle = 0.0F;
    private static final float MAX_ANGLE_CHANGE = 45.0F;
    private int jumpCount = 0;
    public final BooleanValue allowMoreStrafing = new BooleanValue("allow more strafing", this, true);
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> this.jumpCount++;
    private double ue;
    private double velocityX;
    private double velocityZ;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
            this.velocityX = s12packetentityvelocity.getMotionX() / 8000.0;
            this.velocityZ = s12packetentityvelocity.getMotionZ() / 8000.0;
            this.ue = s12packetentityvelocity.getMotionZ() / 8000.0;
        }
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var1x -> {
        double d0 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction()));
        double d1 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(aEg.thePlayer.motionZ, aEg.thePlayer.motionX)) - 90.0);
        double d2 = d0 / 1.4;
        if (aEg.thePlayer.ae > 8) {
            if (aEg.thePlayer.onGround) {
                MoveUtil.strafe();
            }

            if (MathUtil.n(d0, d1) < 90.0) {
                MoveUtil.strafe();
            } else if (this.allowMoreStrafing.wo()) {
                MoveUtil.strafe(MoveUtil.speed() * 0.7);
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.jumpCount % 2 == 1) {
            ;
        }

        Math.hypot(
            aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry), aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
        );
        if ((
                aEg.thePlayer.ae < 8
                    || aEg.thePlayer.onGround
                    || aEg.thePlayer.tR == 1
                    || PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air && aEg.thePlayer.tR > 10
            )
            && (aEg.thePlayer.onGround || aEg.thePlayer.tR == 1 || PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air)
            && aEg.thePlayer.ae >= 8) {
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        MoveUtil.roundToGround(var0.getPosY());
        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }
    };

    public MineMenClubSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    public static void simulateStrafe(MoveEvent moveEvent, double var1, float var3, float var4, float var5) {
        if (var3 != 0.0F || var4 != 0.0F) {
            float f = var5;
            boolean flag = var3 < 0.0F;
            float f1 = 90.0F * (var3 > 0.0F ? 0.5F : (flag ? -0.5F : 1.0F));
            if (flag) {
                f += 180.0F;
            }

            if (var4 > 0.0F) {
                f -= f1;
            } else if (var4 < 0.0F) {
                f += f1;
            }

            float f2 = (f + 360.0F) % 360.0F;
            float f3 = f2 - strafeAngle;
            float f4 = (f3 + 180.0F) % 360.0F - 180.0F;
            if (Math.abs(f4) < 45.0F) {
                strafeAngle = f2;
            } else {
                strafeAngle = strafeAngle + Math.signum(f4) * 45.0F;
            }

            strafeAngle = (strafeAngle + 360.0F) % 360.0F;
            double d0 = StrictMath.cos(Math.toRadians(strafeAngle + 90.0));
            double d1 = StrictMath.cos(Math.toRadians(strafeAngle));
            moveEvent.setPosX(d0 * var1);
            moveEvent.setPosZ(d1 * var1);
        }
    }

    @Override
    public void onEnable() {
        this.velocityX = 0.0;
        this.velocityZ = 0.0;
    }
}
