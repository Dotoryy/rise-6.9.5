package com.alan.clients.module.impl.movement.step;

import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.StepEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class MatrixStep extends Mode<Step> {
    private final BooleanValue twoBlockValue = new BooleanValue("2 Block", this, true);
    private final BooleanValue instantValue = new BooleanValue("Instant", this, true, this.twoBlockValue::wo);
    private int ticks;
    private boolean doJump;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        aEg.thePlayer.stepHeight = this.twoBlockValue.wo() ? 2.0F : 1.0F;
        if (this.doJump) {
            if (this.ticks > 0 && aEg.thePlayer.onGround || this.ticks > 5) {
                this.ticks = 0;
                this.doJump = false;
                return;
            }

            if (this.ticks % 3 == 0) {
                var1x.setOnGround(true);
                aEg.thePlayer.jump();
            }

            this.ticks++;
        }
    };
    @EventLink
    public final Listener<StepEvent> onStep = var1x -> {
        if (var1x.getHeight() > 1.0) {
            if (this.instantValue.wo()) {
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 0.41999998688698, aEg.thePlayer.posZ, false));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 0.7531999805212, aEg.thePlayer.posZ, false));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 1.00133597911215, aEg.thePlayer.posZ, true));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 1.42133596599913, aEg.thePlayer.posZ, false));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 1.75453595963335, aEg.thePlayer.posZ, false));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 2.0026719582243, aEg.thePlayer.posZ, false));
                aEg.timer.dzD = 0.14285715F;
            } else {
                this.doJump = true;
                this.ticks = 0;
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
            }
        } else {
            if (var1x.getHeight() > 0.6F) {
                aEg.timer.dzD = 0.33333F;
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 0.42F, aEg.thePlayer.posZ, false));
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 0.42F, aEg.thePlayer.posZ, true));
            }
        }
    };

    public MatrixStep(String var1, Step step) {
        super(var1, step);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.stepHeight = 0.6F;
    }
}
