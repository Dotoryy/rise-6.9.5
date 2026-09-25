package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class NCPSpeed extends Mode<Speed> {
    private boolean reset;
    private double speed;
    private final NumberValue jumpMotion = new NumberValue("Jump Motion", this, 0.4, 0.4, 0.42, 0.01);
    private final NumberValue groundSpeed = new NumberValue("Ground Speed", this, 1.75, 0.1, 2.5, 0.05);
    private final NumberValue bunnySlope = new NumberValue("Bunny Slope", this, 0.66, 0, 1, 0.01);
    private final NumberValue timer = new NumberValue("Timer", this, 1, 0.1, 10, 0.05);
    private final BooleanValue damageBoost = new BooleanValue("Damage Boost", this, true);
    private final BooleanValue customBoost = new BooleanValue("Custom Boost", this, false);
    private final NumberValue boostSpeed = new NumberValue("Boost Speed", this, 0.8, 0.1, 9.5, 0.1);
    private final BooleanValue lowHop = new BooleanValue("Low Hop", this, false);
    private final BooleanValue yPortHop = new BooleanValue("Y-port Hop", this, false);
    private final NumberValue hurtTime = new NumberValue("Hurt Time", this, 6, 1, 10, 1);
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (this.damageBoost.wo()) {
            if (var1x.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                this.speed = Math.hypot(s12packetentityvelocity.motionX / 8000.0, s12packetentityvelocity.motionZ / 8000.0);
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.lowHop.wo() && aEg.thePlayer.tR == 4) {
            aEg.thePlayer.motionY = -0.09800000190734864;
        }

        if (this.yPortHop.wo() && aEg.thePlayer.tR == 5 && Math.abs(aEg.thePlayer.motionY - 0.09800000190734864) < 0.12) {
            aEg.thePlayer.motionY = -0.09800000190734864;
        }

        if (this.customBoost.wo() && aEg.thePlayer.crG <= this.hurtTime.wo().intValue()) {
            this.speed = this.boostSpeed.wo().doubleValue();
        }

        double d0 = MoveUtil.getAllowedHorizontalDistance();
        if (MoveUtil.isMoving()) {
            switch (aEg.thePlayer.tR) {
                case 0:
                    float f = this.jumpMotion.wo().floatValue();
                    float f1 = aEg.thePlayer.isCollidedHorizontally ? 0.42F : (f == 0.4F ? f : 0.42F);
                    aEg.thePlayer.motionY = MoveUtil.jumpBoostMotion(f1);
                    this.speed = d0 * this.groundSpeed.wo().doubleValue();
                    break;
                case 1:
                    this.speed = this.speed - this.bunnySlope.wo().doubleValue() * (this.speed - d0);
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

        if (aEg.thePlayer.isCollidedHorizontally) {
            this.speed = MoveUtil.getAllowedHorizontalDistance();
        }

        var1x.setSpeed(Math.max(this.speed, d0), Math.random() / 2000.0);
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.speed = 0.0;

    public NCPSpeed(String var1, Speed var2) {
        super(var1, var2);
    }

    @Override
    public void onDisable() {
        this.speed = 0.0;
    }
}
