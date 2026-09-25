package com.alan.clients.module.impl.combat.criticals;

import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import rip.vantage.commons.util.time.StopWatch;

public final class WatchdogCriticals extends Mode<Criticals> {
    private final NumberValue delay = new NumberValue("Delay", this, 500, 0, 1000, 50);
    private final double[] offsets = new double[]{1.0E-6, MoveUtil.predictedMotion(0.03125, 1)};
    private final StopWatch stopwatch = new StopWatch();
    public static boolean gD;
    public static boolean pendingCrit;
    private int critTicks;
    private int ticksSinceCrit;
    private int rX;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var0 -> {};
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.onGround && pendingCrit) {
            aEg.thePlayer.crd = true;
            aEg.thePlayer.stepHeight = 0.1F;
            this.critTicks++;
            this.ticksSinceCrit = 0;
            if (this.critTicks > 2) {
                this.ticksSinceCrit++;
            }

            switch (this.critTicks) {
                case 1:
                    var1x.setPosY(var1x.getPosY() + 0.001);
                    break;
                case 2:
                    pendingCrit = false;
            }

            var1x.setOnGround(false);
            if (this.critTicks > 4) {
                aEg.thePlayer.stepHeight = 0.6F;
            }

            if (aEg.thePlayer.onGround && this.critTicks < 6) {
                aEg.thePlayer.crd = true;
            }

            if (this.critTicks > 6) {
                aEg.thePlayer.crd = false;
            }
        } else {
            this.ticksSinceCrit++;
            if (aEg.thePlayer.csk > 4 || this.ticksSinceCrit > 4) {
                aEg.thePlayer.stepHeight = 0.6F;
            }

            if (this.ticksSinceCrit < 5 && aEg.thePlayer.onGround && this.critTicks < 6) {
                aEg.thePlayer.crd = true;
            } else {
                aEg.thePlayer.crd = false;
            }

            pendingCrit = false;
            this.critTicks = 0;
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1x -> {
        if (aEg.thePlayer.onGround && !aEg.thePlayer.isOnLadder() && this.stopwatch.T(this.delay.wo().longValue())) {
            aEg.thePlayer.onCriticalHit(var1x.getLiving());
            this.stopwatch.aX();
            pendingCrit = true;
        }
    };
    @EventLink(value = 4)
    public final Listener<JumpEvent> onJump = var1x -> {
        if (this.critTicks <= 4 && this.ticksSinceCrit <= 4 && aEg.thePlayer.cqL > 2) {
            var1x.setCancelled();
        }
    };

    public WatchdogCriticals(String var1, Criticals criticals) {
        super(var1, criticals);
    }
}
