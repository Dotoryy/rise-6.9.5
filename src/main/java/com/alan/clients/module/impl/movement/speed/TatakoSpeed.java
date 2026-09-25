package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import net.minecraft.util.Vec3;

public class TatakoSpeed extends Mode<Speed> {
    public Vec3 Jd = new Vec3(0.0, 0.0, 0.0);
    int strafeTicks = 0;
    float forward = 0.0F;
    float strafe = 0.0F;
    @EventLink(value = 3)
    Listener<MoveInputEvent> onMoveInput = var1x -> {
        this.forward = var1x.getForward();
        this.strafe = var1x.getStrafe();
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (MoveUtil.isMoving() && aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
            this.strafeTicks++;
        }

        if (aEg.thePlayer.onGround) {
            MoveUtil.moveFlying(0.005);
            MoveUtil.partialStrafePercent(95.0);
        }

        if (aEg.thePlayer.tR == 1) {
            MoveUtil.partialStrafePercent(60.0);
        }

        if (aEg.thePlayer.tR == 2) {
            MoveUtil.partialStrafePercent(50.0);
        } else {
            MoveUtil.partialStrafePercent(1.0);
        }

        if (aEg.thePlayer.tR == 1) {
            aEg.thePlayer.motionY = MoveUtil.predictedMotion(aEg.thePlayer.motionY + 0.12, 2);
        }

        if (Math.hypot(
                aEg.thePlayer.motionX - (aEg.thePlayer.lastTickPosX - aEg.thePlayer.cry),
                aEg.thePlayer.motionZ - (aEg.thePlayer.lastTickPosZ - aEg.thePlayer.crA)
            )
            < 0.005) {
            MoveUtil.strafe();
        }

        MoveUtil.moveFlying(8.0E-4);
        MoveUtil.preventDiagonalSpeed();
    };
    @EventLink(value = 1)
    Listener<PreUpdateEvent> onPreUpdate = var1x -> RotationComponent.setRotations(
        new Vector2f((float)Math.toDegrees(MoveUtil.g(this.forward, this.strafe)), aEg.thePlayer.rotationPitch), 10.0, MovementFix.OFF
    );

    public TatakoSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    @Override
    public void onEnable() {
        this.strafeTicks = 0;
    }
}
