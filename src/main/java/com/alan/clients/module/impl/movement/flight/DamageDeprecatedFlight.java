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
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.DamageType;

public class DamageDeprecatedFlight extends Mode<Flight> {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        float f = this.speed.wo().floatValue();
        var1x.setSpeed(f);
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        float f = this.speed.wo().floatValue();
        aEg.thePlayer.motionY = 0.0 + (aEg.gameSettings.keyBindJump.isKeyDown() ? f : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? f : 0.0);
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> var0.setSneak(false);

    public DamageDeprecatedFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        DamageUtil.damagePlayer(DamageType.POSITION, 3.42F, 1, false, false);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
