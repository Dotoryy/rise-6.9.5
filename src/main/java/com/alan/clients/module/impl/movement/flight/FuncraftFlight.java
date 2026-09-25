package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public final class FuncraftFlight extends Mode<Flight> {
    private final NumberValue speed = new NumberValue("Speed", this, 1.2, 0.8, 2, 0.05);
    private final BooleanValue vanillaKickBypass = new BooleanValue("Vanilla Kick Bypass", this, true);
    private double moveSpeed;
    private int stage;
    private int ticks;
    @EventLink
    private final Listener<PreMotionEvent> preMotionEventListener = var0 -> var0.setOnGround(true);
    @EventLink(value = 4)
    private final Listener<MoveEvent> moveEventListener = var1x -> {
        if (!MoveUtil.isMoving() || aEg.thePlayer.isCollidedHorizontally) {
            this.stage = -1;
        }

        if (this.vanillaKickBypass.wo() && this.ticks > 125) {
            this.stage = -1;
            this.ticks = 0;
            PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX + 5.0, aEg.thePlayer.posY + 1.0, aEg.thePlayer.posZ + 5.0, true));
        } else {
            switch (this.stage) {
                case -1:
                    aEg.thePlayer.motionY = 0.0;
                    var1x.setPosY(-1.0E-5);
                    return;
                case 0:
                    this.moveSpeed = 0.3;
                    break;
                case 1:
                    if (aEg.thePlayer.onGround) {
                        var1x.setPosY(aEg.thePlayer.motionY = 0.3999);
                        this.moveSpeed *= 2.14;
                    }
                    break;
                case 2:
                    this.moveSpeed = this.speed.wo().doubleValue();
                    break;
                default:
                    this.moveSpeed = this.moveSpeed - this.moveSpeed / 109.0;
                    aEg.thePlayer.motionY = 0.0;
                    var1x.setPosY(-1.0E-5);
            }

            aEg.thePlayer.jumpMovementFactor = 0.0F;
            MoveUtil.setMoveEvent(var1x, Math.max(this.moveSpeed, MoveUtil.getAllowedHorizontalDistance()));
            this.stage++;
        }
    };

    public FuncraftFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        this.moveSpeed = 0.0;
        this.stage = aEg.thePlayer.onGround ? 0 : -1;
        this.ticks = 0;
    }
}
