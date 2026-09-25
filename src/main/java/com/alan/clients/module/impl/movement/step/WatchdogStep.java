package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class WatchdogStep extends Mode<Step> {
    private boolean RW;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {};
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {};
    private double height;
    private int ticksSinceStep;
    private long speedReEnableTime;
    private boolean speedDisabled = false;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionMedium = var1x -> {
        if (!this.e(Scaffold.class).isEnabled()) {
            if (aEg.thePlayer.onGround && !PlayerUtil.vj() && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                aEg.thePlayer.stepHeight = 1.0F;
            } else {
                aEg.thePlayer.stepHeight = 0.6F;
            }
        }
    };
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        MoveUtil.strafe(MoveUtil.vd());
        double d0 = var1x.getHeight();
        this.height = d0;
        if (this.e(Speed.class).isEnabled() && d0 > 0.6F) {
            this.e(Speed.class).setEnabled(false);
            this.speedDisabled = true;
        }

        if (!this.e(Scaffold.class).isEnabled()) {
            aEg.thePlayer.stepHeight = 1.0F;
            if (aEg.thePlayer.onGround
                && !PlayerUtil.vj()
                && !(PlayerUtil.o(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ) instanceof BlockSlab)
                && !(PlayerUtil.o(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ) instanceof BlockStairs)) {
                if (!(d0 <= 0.6F)) {
                    double[] adouble = new double[0];
                    if (d0 == 1.0) {
                        adouble = new double[]{0.42F, 0.75, 1.0};
                    }

                    this.speedReEnableTime = System.currentTimeMillis() + 300L;

                    for (double d1 : adouble) {
                        aEg.timer.dzD = 0.25F;
                        PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + d1, aEg.thePlayer.posZ, false));
                        this.ticksSinceStep = 0;
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        this.ticksSinceStep++;
        if (this.ticksSinceStep == 1) {
            aEg.timer.dzD = 1.0F;
        }

        if (this.speedDisabled && System.currentTimeMillis() > this.speedReEnableTime) {
            this.e(Speed.class).setEnabled(true);
            this.speedDisabled = false;
            this.speedReEnableTime = 0L;
        }
    };

    public WatchdogStep(String var1, Step step) {
        super(var1, step);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.stepHeight = 0.6F;
        this.speedDisabled = false;
        this.speedReEnableTime = 0L;
    }
}
