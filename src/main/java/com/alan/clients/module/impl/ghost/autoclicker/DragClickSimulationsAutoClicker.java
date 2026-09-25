package com.alan.clients.module.impl.ghost.autoclicker;

import com.alan.clients.module.impl.ghost.AutoClicker;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.util.player.PlayerUtil;

public class DragClickSimulationsAutoClicker extends Mode<AutoClicker> {
    private final BoundsNumberValue length = new BoundsNumberValue("Drag Click Length", this, 17, 18, 1, 50, 1);
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay Between Dragging", this, 6, 6, 1, 20, 1);
    private int lengthTicks;
    private int delayTicks;
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        if (aEg.gameSettings.cgK.isKeyDown()) {
            if (this.lengthTicks < 0) {
                this.delayTicks--;
                if (this.delayTicks < 0) {
                    this.delayTicks = this.delay.wv().intValue();
                    this.lengthTicks = this.length.wv().intValue();
                }
            } else if (Math.random() < 0.95) {
                this.lengthTicks--;
                PlayerUtil.sendClick(0, true);
            }
        }
    };

    public DragClickSimulationsAutoClicker(String var1, AutoClicker autoClicker) {
        super(var1, autoClicker);
    }
}
