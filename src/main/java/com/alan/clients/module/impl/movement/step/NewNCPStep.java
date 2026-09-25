package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class NewNCPStep extends Mode<Step> {
    private final NumberValue height = new NumberValue("Height", this, 1, 1, 1.5, 0.1);
    private final NumberValue timer = new NumberValue("Timer", this, 0.5, 0.1, 1, 0.1);
    private double stepHeight;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.onGround && !PlayerUtil.vj()) {
            aEg.thePlayer.stepHeight = this.height.wo().floatValue();
        } else {
            aEg.thePlayer.stepHeight = 0.6F;
        }
    };
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        if (aEg.thePlayer.onGround && !PlayerUtil.vj()) {
            double d0 = var1x.getHeight();
            this.stepHeight = d0;
            if (!(d0 <= 0.6F)) {
                double[] adouble;
                if (d0 > 1.015) {
                    adouble = new double[]{0.42F, 0.7532F, 1.0, 0.98F};
                } else if (d0 > 0.875) {
                    adouble = new double[]{0.42F, 0.7532F, 1.0};
                } else {
                    adouble = new double[]{0.39F, 0.6938F};
                }

                aEg.timer.dzD = this.timer.wo().floatValue();

                for (double d1 : adouble) {
                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + d1, aEg.thePlayer.posZ, false));
                }
            }
        }
    };
    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = var1x -> {
        double[] adouble;
        if (this.stepHeight > 1.015) {
            adouble = new double[]{0.42F, 0.7532F, 1.0, 0.98F};
        } else if (this.stepHeight > 0.875) {
            adouble = new double[]{0.42F, 0.7532F, 1.0};
        } else {
            adouble = new double[]{0.39F, 0.6938F};
        }

        if (var1x.dq() instanceof C04PacketPlayerPosition && PlayerUtil.vg()) {
            for (double d0 : adouble) {
                ;
            }
        }
    };

    public NewNCPStep(String var1, Step step) {
        super(var1, step);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.stepHeight = 0.6F;
    }
}
