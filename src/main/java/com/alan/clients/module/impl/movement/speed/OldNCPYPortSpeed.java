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
import com.alan.clients.value.impl.BooleanValue;

public class OldNCPYPortSpeed extends Mode<Speed> {
    public final BooleanValue smoothCamera = new BooleanValue("Smooth Camera", this, false);
    private boolean Lw;
    private double speed;
    private boolean HJ;
    private double QB;
    private int stage;
    private double lastHorizontalDistance;
    private final double BASE_SPEED = 0.2873;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        double d0 = aEg.thePlayer.posX - aEg.thePlayer.lastTickPosX;
        double d1 = aEg.thePlayer.posZ - aEg.thePlayer.lastTickPosZ;
        this.lastHorizontalDistance = Math.sqrt(d0 * d0 + d1 * d1);
        if (this.stage == 3) {
            var1x.setPosY(var1x.getPosY() + 0.4);
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> var0.setJump(false);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        MoveUtil.isMoving();
        switch (this.stage) {
            case 2:
                this.speed *= 2.14;
                this.stage = 3;
                break;
            case 3:
                this.stage = 2;
                double d0 = 0.66 * (this.lastHorizontalDistance - 0.2873);
                this.speed = this.lastHorizontalDistance - d0;
                break;
            default:
                if (aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, aEg.thePlayer.getEntityBoundingBox().offset(0.0, aEg.thePlayer.motionY, 0.0)).size()
                        > 0
                    || aEg.thePlayer.isCollidedVertically) {
                    this.stage = 1;
                }

                this.speed = this.lastHorizontalDistance - this.lastHorizontalDistance / 159.0;
        }

        if (aEg.thePlayer.isCollidedHorizontally) {
            this.speed = 0.2873;
        }

        if (aEg.thePlayer.tR == 1 && aEg.thePlayer.Zl > 2) {
            this.speed = 0.3873;
        }

        if (!aEg.thePlayer.onGround) {
            this.stage++;
        }

        var1x.setSpeed(this.speed);
        MoveUtil.preventDiagonalSpeed();
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInputMedium = var0 -> var0.setJump(false);
    @EventLink
    public final Listener<JumpEvent> onJump = var0 -> var0.setJumpMotion(0.4F);

    public OldNCPYPortSpeed(String var1, Speed speed) {
        super(var1, speed);
    }

    @Override
    public void onDisable() {
        this.speed = 0.0;
    }

    @Override
    public void onEnable() {
        this.stage = 2;
        this.speed = 0.2873;
        this.lastHorizontalDistance = 0.0;
    }
}
