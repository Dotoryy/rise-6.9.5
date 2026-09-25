package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class MatrixSpeed extends Mode<Speed> {
    private final NumberValue timerBoostOnSneak = new NumberValue("Timer Boost (on sneak)", this, 30, 1, 100, 0.1);
    private int ticksSinceSneak = 0;
    @EventLink
    public final Listener<JumpEvent> onJump = var1x -> this.ticksSinceSneak++;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        boolean flag = aEg.thePlayer.posY % 0.015625 == 0.0;
        boolean flag1 = MoveUtil.speed() < 0.2;
        if (flag || flag1) {
            MoveUtil.strafe();
        }

        aEg.thePlayer.isUsingItem();
        if (MoveUtil.speed() < 0.195 && !aEg.thePlayer.isUsingItem()) {
            MoveUtil.strafe(0.195);
        }

        if (aEg.thePlayer.onGround) {
            aEg.thePlayer.motionX *= 1.001;
            aEg.thePlayer.motionZ *= 1.001;
            MoveUtil.strafe();
        }

        if (aEg.thePlayer.ae > 1) {
            aEg.thePlayer.motionY -= 0.00348;
        }

        if (aEg.thePlayer.tR == 1) {
            MoveUtil.partialStrafePercent(65.0);
        }

        if (PlayerUtil.ae(aEg.thePlayer.motionY)) {
            MoveUtil.partialStrafePercent(65.0);
        }

        if (aEg.thePlayer.ae < 12) {
            MoveUtil.strafe();
        }

        MoveUtil.useDiagonalSpeed();
        if (aEg.gameSettings.keyBindSneak.isKeyDown()) {
            this.ticksSinceSneak = 0;
            MoveUtil.strafe(0.07);
            aEg.timer.dzD = this.timerBoostOnSneak.wo().floatValue();
        } else {
            this.ticksSinceSneak++;
            if (this.ticksSinceSneak == 1) {
                PacketUtil.sendNoEvent(
                    new C06PacketPlayerPosLook(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false)
                );
                PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, false));
                PacketUtil.sendNoEvent(
                    new C06PacketPlayerPosLook(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false)
                );
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        var0.setSneak(false);
        var0.setJump(true);
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.thePlayer.onGround && !aEg.thePlayer.isUsingItem()) {
            aEg.thePlayer.jump();
        }
    };

    public MatrixSpeed(String var1, Speed speed) {
        super(var1, speed);
    }
}
