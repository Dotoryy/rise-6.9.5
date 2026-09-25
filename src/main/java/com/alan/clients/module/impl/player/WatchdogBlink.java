package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.NumberValue;
import java.util.ArrayDeque;
import java.util.Deque;

@ModuleInfo(aliases = "module.player.watchdogblink.name", description = "module.player.watchdogblink.description", category = Category.PLAYER)
public class WatchdogBlink extends Module {
    public NumberValue interval = new NumberValue("Interval", this, 4, 1, 20, 1);
    public NumberValue disableTicks = new NumberValue("Disable Ticks", this, 15, 1, 40, 1);
    private final Deque<Integer> packetCounts = new ArrayDeque<>();
    private int lastQueueSize;
    private int ticks;
    private boolean flushing;
    private boolean selfDisabling;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> BlinkComponent.blink();
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1 -> {
        int i = BlinkComponent.bg() - this.lastQueueSize;
        if (i > 0) {
            this.packetCounts.add(i);
        }

        if (this.flushing) {
            this.releaseTicks(this.disableTicks.wo().intValue());
            if (this.packetCounts.isEmpty() && !BlinkComponent.bh()) {
                this.selfDisabling = true;
                super.setEnabled(false);
                this.selfDisabling = false;
                return;
            }
        } else {
            this.ticks++;
            if (this.ticks >= this.interval.wo().intValue()) {
                this.ticks = 0;
                this.releaseTicks(1);
            }
        }

        this.lastQueueSize = BlinkComponent.bg();
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> {
        this.packetCounts.clear();
        this.ticks = 0;
        this.flushing = false;
        this.lastQueueSize = BlinkComponent.bg();
    };

    public WatchdogBlink() {
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled && this.isEnabled() && !this.selfDisabling) {
            if (this.flushing) {
                return;
            }

            if (!this.packetCounts.isEmpty() || BlinkComponent.bh()) {
                this.flushing = true;
                this.ticks = 0;
                return;
            }
        }

        super.setEnabled(enabled);
    }

    @Override
    public void onEnable() {
        this.flushing = false;
        this.selfDisabling = false;
        this.packetCounts.clear();
        this.lastQueueSize = BlinkComponent.bg();
        this.ticks = 0;
    }

    @Override
    public void onDisable() {
        this.flushing = false;
        this.selfDisabling = false;
        this.packetCounts.clear();
        this.ticks = 0;
        BlinkComponent.dispatch();
    }

    private void releaseTicks(int var1) {
        for (int i = 0; i < var1; i++) {
            Integer integer = this.packetCounts.poll();
            if (integer == null) {
                break;
            }

            if (integer > 0) {
                this.releasePackets(integer);
            }
        }
    }

    private void releasePackets(int var1) {
        BlinkComponent.d(var1);
    }
}
