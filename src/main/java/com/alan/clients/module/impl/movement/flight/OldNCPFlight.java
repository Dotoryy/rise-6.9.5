package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import org.apache.commons.lang3.RandomUtils;

public class OldNCPFlight extends Mode<Flight> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        var0.setPosY(
            var0.getPosY()
                + 1.0E-5
                + (aEg.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextDouble(1.0E-10, 1.0E-5) : -RandomUtils.nextDouble(1.0E-10, 1.0E-5))
        );
        aEg.thePlayer.motionY = 0.0;
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> var0.setSpeed(MoveUtil.getAllowedHorizontalDistance(), Math.random() / 2000.0);

    public OldNCPFlight(String var1, Flight flight) {
        super(var1, flight);
    }
}
