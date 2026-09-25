package com.alan.clients.module.impl.combat.criticals;

import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import rip.vantage.commons.util.time.StopWatch;

public final class EditCriticals extends Mode<Criticals> {
    private final NumberValue delay = new NumberValue("Delay", this, 500, 0, 1000, 50);
    private final double[] VALUES = new double[]{5.0E-4, 1.0E-4};
    private final StopWatch stopwatch = new StopWatch();
    private boolean attacked;
    private int ticks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.onGround && this.attacked) {
            this.ticks++;
            switch (this.ticks) {
                case 1:
                    var1x.setPosY(var1x.getPosY() + this.VALUES[0]);
                    break;
                case 2:
                    var1x.setPosY(var1x.getPosY() + this.VALUES[1]);
                    this.attacked = false;
            }

            var1x.setOnGround(false);
        } else {
            this.attacked = false;
            this.ticks = 0;
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttackEvent = var1x -> {
        if (aEg.thePlayer.onGround && !aEg.thePlayer.isOnLadder() && this.stopwatch.T(this.delay.wo().longValue())) {
            aEg.thePlayer.onCriticalHit(var1x.getLiving());
            this.stopwatch.aX();
            this.attacked = true;
        }
    };

    public EditCriticals(String var1, Criticals criticals) {
        super(var1, criticals);
    }
}
