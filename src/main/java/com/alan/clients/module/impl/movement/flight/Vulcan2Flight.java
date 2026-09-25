package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class Vulcan2Flight extends Mode<Flight> {
    private static final double HD = 10.0;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        BlinkComponent.a(25000, true, false, false, false);
        if (MoveUtil.speed() < 0.2) {
            MoveUtil.strafe(0.2);
        }

        MoveUtil.useDiagonalSpeed();
        if (aEg.thePlayer.ticksExisted % 3 == 0) {
            aEg.thePlayer.motionY = -0.15;
        } else {
            aEg.thePlayer.motionY = -0.09701;
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {};
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> var0.setSneak(false);

    public Vulcan2Flight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
