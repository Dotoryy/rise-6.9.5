package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;

public class VanillaFlight extends Mode<Flight> {
    private static final double MAX_SPEED = 10.0;
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 10, 0.1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        float f = this.speed.wo().floatValue();
        double d0 = (aEg.gameSettings.keyBindJump.isKeyDown() ? f : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? f : 0.0);
        aEg.thePlayer.motionY = d0;
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        float f = this.speed.wo().floatValue();
        double d0 = aEg.thePlayer.motionY;
        double d1 = Math.sqrt(f * f + d0 * d0);
        if (d1 > 10.0) {
            double d2 = 10.0 / d1;
            f = (float)(f * d2);
            d0 *= d2;
        }

        var1x.setSpeed(f);
        aEg.thePlayer.motionY = d0;
    };
    @EventLink
    public final Listener<MoveInputEvent> onMove = var0 -> var0.setSneak(false);

    public VanillaFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
