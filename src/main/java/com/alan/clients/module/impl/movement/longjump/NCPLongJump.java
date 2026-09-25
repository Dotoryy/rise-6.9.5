package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public class NCPLongJump extends Mode<LongJump> {
    private final NumberValue bunnyFriction = new NumberValue("Bunny Friction", this, 159, 59, 259, 1);
    private final NumberValue groundSpeed = new NumberValue("Ground Speed", this, 0.4, 0.1, 3, 0.1);
    private final NumberValue jumpSpeed = new NumberValue("Jump Speed", this, 1.4, 0, 3, 0.1);
    private final NumberValue glide = new NumberValue("Glide", this, 0, 0, 3, 0.5);
    private final NumberValue timer = new NumberValue("Timer", this, 1, 0.1, 10, 0.1);
    private boolean reset;
    private double speed;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        double d0 = MoveUtil.getAllowedHorizontalDistance();
        if (MoveUtil.isMoving()) {
            switch (aEg.thePlayer.tR) {
                case 0:
                    aEg.thePlayer.motionY = MoveUtil.jumpMotion() + 0.017;
                    this.speed = this.groundSpeed.wo().doubleValue();
                    break;
                case 1:
                    this.speed = this.jumpSpeed.wo().doubleValue();
                    break;
                default:
                    this.speed = this.speed - this.speed / 159.9F;
            }

            aEg.timer.dzD = this.timer.wo().floatValue();
            this.reset = false;
        } else if (!this.reset) {
            this.speed = MoveUtil.getAllowedHorizontalDistance();
            aEg.timer.dzD = 1.0F;
            this.reset = true;
        }

        if (aEg.thePlayer.fallDistance > 0.0F) {
            aEg.thePlayer.motionY = aEg.thePlayer.motionY + this.glide.wo().floatValue() / 100.0F;
        }

        if (aEg.thePlayer.isCollidedHorizontally) {
            this.speed = MoveUtil.getAllowedHorizontalDistance();
        }

        var1x.setSpeed(Math.max(this.speed, d0), Math.random() / 2000.0);
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.speed = 0.0;

    public NCPLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
        this.speed = 0.0;
    }
}
