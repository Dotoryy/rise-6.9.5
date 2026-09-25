package com.alan.clients.module.impl.ghost.autoclicker;

import com.alan.clients.module.impl.ghost.AutoClicker;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import rip.vantage.commons.util.time.StopWatch;

public class NormalAutoClicker extends Mode<AutoClicker> {
    private final BoundsNumberValue cps = new BoundsNumberValue("CPS", this, 8, 14, 1, 20, 0.1);
    private final BooleanValue rightClick = new BooleanValue("Right Click", this, false);
    private final BooleanValue leftClick = new BooleanValue("Left Click", this, true);
    private final BooleanValue hitSelect = new BooleanValue("Hit Select", this, false);
    private final BooleanValue breakBlocks = new BooleanValue("Break Blocks", this, true);
    private final BooleanValue butterFly = new BooleanValue("ButterFly", this, true);
    private final StopWatch clickStopWatch = new StopWatch();
    private int ticksDown;
    private int attackTicks;
    private long nextSwing;
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        this.attackTicks++;
        if (this.clickStopWatch.T(this.nextSwing) && (!this.hitSelect.wo() || this.attackTicks >= 10 || aEg.thePlayer.hurtTime > 0 && this.clickStopWatch.T(this.nextSwing)) && aEg.currentScreen == null) {
            long i = (long)(this.cps.wv().longValue() * 1.5);
            if (aEg.gameSettings.cgK.isKeyDown()) {
                this.ticksDown++;
            } else {
                this.ticksDown = 0;
            }

            if (this.nextSwing >= 100L && this.butterFly.wo()) {
                this.nextSwing = (long)(Math.random() * 100.0);
            } else {
                this.nextSwing = 1000L / i;
            }

            if (this.rightClick.wo() && aEg.gameSettings.cgI.isKeyDown() && !aEg.gameSettings.cgK.isKeyDown()) {
                PlayerUtil.sendClick(1, true);
                if (Math.random() > 0.9) {
                    PlayerUtil.sendClick(1, true);
                }
            }

            if (!this.leftClick.wo()
                || this.ticksDown <= 1
                || aEg.gameSettings.cgI.isKeyDown()
                || this.breakBlocks.wo() && aEg.objectMouseOver != null && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
                if (!this.breakBlocks.wo()) {
                    aEg.playerController.curBlockDamageMP = 0.0F;
                }
            } else {
                PlayerUtil.sendClick(0, true);
            }

            this.clickStopWatch.aX();
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1x -> this.attackTicks = 0;

    public NormalAutoClicker(String var1, AutoClicker autoClicker) {
        super(var1, autoClicker);
    }
}
