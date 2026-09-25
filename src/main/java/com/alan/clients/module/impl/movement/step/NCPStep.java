package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class NCPStep extends Mode<Step> {
    private final NumberValue height = new NumberValue("Height", this, 1, 1, 2.5, 0.1);
    private final NumberValue timer = new NumberValue("Timer", this, 0.5, 0.1, 1, 0.1);
    private final BooleanValue reverse = new BooleanValue("Reverse", this, false);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.onGround && !PlayerUtil.vj()) {
            aEg.thePlayer.stepHeight = this.height.wo().floatValue();
        } else {
            aEg.thePlayer.stepHeight = 0.6F;
        }

        if (this.reverse.wo() && !(PlayerUtil.p(0.0, -(this.height.wo().intValue() + 1), 0.0) instanceof BlockAir) && !PlayerUtil.vj()) {
            for (int i = 1; i < this.height.wo().intValue() + 1; i++) {
                aEg.thePlayer.motionY -= i;
            }
        }
    };
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        if (aEg.thePlayer.onGround && !PlayerUtil.vj()) {
            double d0 = var1x.getHeight();
            if (!(d0 <= 0.6)) {
                double[] adouble;
                if (d0 > 2.019) {
                    adouble = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
                } else if (d0 > 1.869) {
                    adouble = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
                } else if (d0 > 1.5) {
                    adouble = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652};
                } else if (d0 > 1.015) {
                    adouble = new double[]{0.42, 0.7532, 1.01, 1.093, 1.015};
                } else if (d0 > 0.875) {
                    adouble = new double[]{0.42, 0.7532};
                } else {
                    adouble = new double[]{0.39, 0.6938};
                }

                aEg.timer.dzD = this.timer.wo().floatValue();

                for (double d1 : adouble) {
                    PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + (d1 + Math.random() / 2000.0), aEg.thePlayer.posZ, false));
                }
            }
        }
    };

    public NCPStep(String var1, Step step) {
        super(var1, step);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.stepHeight = 0.6F;
    }
}
