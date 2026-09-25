package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.PlayerUtil;

public class VanillaStep extends Mode<Step> {
    private final NumberValue height = new NumberValue("Height", this, 1, 1, 10, 0.1);
    private final BooleanValue reverse = new BooleanValue("Reverse", this, false);
    private final NumberValue timer = new NumberValue("Timer", this, 0.5, 0.1, 1, 0.1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        aEg.thePlayer.stepHeight = this.height.wo().floatValue();
        if (this.reverse.wo() && PlayerUtil.ad(this.height.wo().floatValue() + aEg.thePlayer.getEyeHeight()) && !PlayerUtil.vj()) {
            if (aEg.thePlayer.posY < aEg.thePlayer.aI && !aEg.thePlayer.onGround && aEg.thePlayer.tR <= 1) {
                aEg.thePlayer.motionY = -this.height.wo().doubleValue();
            }

            if (aEg.thePlayer.tR == 1 && aEg.thePlayer.posY < aEg.thePlayer.cqY) {
                aEg.timer.dzD = this.timer.wo().floatValue();
            }
        }
    };
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        if (var1x.getHeight() > 0.6) {
            aEg.timer.dzD = this.timer.wo().floatValue();
        }
    };

    public VanillaStep(String var1, Step step) {
        super(var1, step);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.stepHeight = 0.6F;
    }
}
