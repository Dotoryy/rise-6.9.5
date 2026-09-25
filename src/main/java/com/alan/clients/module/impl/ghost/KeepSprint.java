package com.alan.clients.module.impl.ghost;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.HitSlowDownEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;

@ModuleInfo(aliases = "module.ghost.keepsprint.name", description = "module.ghost.keepsprint.description", category = Category.GHOST)
public class KeepSprint extends Module {
    private final NumberValue slowDownVelocity = new NumberValue("Hit Slow Down During Velocity", this, 0.6, 0, 1, 0.05);
    private final NumberValue slowDownNormal = new NumberValue("Hit Slow Down Normal", this, 0.6, 0, 1, 0.05);
    private final NumberValue bufferDecrease = new NumberValue("Buffer Decrease", this, 1, 0.1, 10, 0.1, () -> !this.bufferAbuse.wo());
    private final NumberValue maxBuffer = new NumberValue("Max Buffer", this, 5, 1, 10, 1, () -> !this.bufferAbuse.wo());
    private final BooleanValue sprintSlowDownVelocity = new BooleanValue("Velocity Hit Sprint", this, false);
    private final BooleanValue sprintSlowDownNormal = new BooleanValue("Normal Hit Sprint", this, false);
    private final BooleanValue bufferAbuse = new BooleanValue("Buffer Abuse", this, false);
    private final BooleanValue onlyInAir = new BooleanValue("Only In Air", this, false);
    private boolean resetting;
    private double combo;
    @EventLink
    public final Listener<HitSlowDownEvent> onHitSlowDown = var1 -> {
        if (!aEg.thePlayer.onGround || !this.onlyInAir.wo()) {
            if (this.bufferAbuse.wo()) {
                if (this.combo < this.maxBuffer.wo().intValue() && !this.resetting) {
                    this.combo++;
                } else {
                    if (this.combo > 0.0) {
                        this.combo = Math.max(0.0, this.combo - this.bufferDecrease.wo().doubleValue());
                        this.resetting = true;
                        return;
                    }

                    this.resetting = false;
                }
            } else {
                this.combo = 0.0;
            }

            if (aEg.thePlayer.hurtTime > 0) {
                var1.setSlowDown(this.slowDownVelocity.wo().doubleValue());
                var1.setSprint(this.sprintSlowDownVelocity.wo());
            } else {
                var1.setSlowDown(this.slowDownNormal.wo().doubleValue());
                var1.setSprint(this.sprintSlowDownNormal.wo());
            }
        }
    };

    public KeepSprint() {
    }
}
