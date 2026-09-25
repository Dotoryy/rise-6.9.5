package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public final class SkyCaveFlight extends Mode<Flight> {
    private final NumberValue speed = new NumberValue("Speed", this, 1.2, 0.8, 10, 0.05);
    private final NumberValue damageSpeed = new NumberValue("Damage Speed", this, 5, 0.8, 10, 0.05);
    private double currentSpeed;
    private int stage;
    @EventLink(value = 4)
    private final Listener<MoveEvent> onMove = var1x -> {
        if (!MoveUtil.isMoving() || aEg.thePlayer.isCollidedHorizontally) {
            this.stage = -1;
        }

        if (aEg.thePlayer.ae == 1) {
            this.currentSpeed = this.damageSpeed.wo().doubleValue();
        }

        switch (this.stage) {
            case -1:
                aEg.thePlayer.motionY = 0.0;
                var1x.setPosY(-1.0E-5);
                break;
            case 0:
                this.currentSpeed = 0.3;
                break;
            case 1:
                if (aEg.thePlayer.onGround) {
                    var1x.setPosY(aEg.thePlayer.motionY = 0.3999);
                    this.currentSpeed *= 2.14;
                }
                break;
            case 2:
                this.currentSpeed = this.speed.wo().doubleValue();
                break;
            default:
                this.currentSpeed = this.currentSpeed - this.currentSpeed / 109.0;
                aEg.thePlayer.motionY = 0.0;
                var1x.setPosY(-1.0E-5);
        }

        if (aEg.thePlayer.ticksExisted % 20 == 0) {
            var1x.setPosY(-0.035);
        }

        if (this.stage != -1) {
            aEg.thePlayer.jumpMovementFactor = 0.0F;
            MoveUtil.setMoveEvent(var1x, Math.max(this.currentSpeed, MoveUtil.getAllowedHorizontalDistance()));
            this.stage++;
        }
    };

    public SkyCaveFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.currentSpeed = 0.0;
        this.stage = aEg.thePlayer.onGround ? 0 : -1;
    }
}
