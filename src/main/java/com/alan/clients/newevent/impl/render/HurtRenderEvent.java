package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;

public final class HurtRenderEvent extends CancellableEvent {
    private boolean oldDamage;

    @Generated
    public boolean dw() {
        return this.oldDamage;
    }

    @Generated
    public void setOldDamage(boolean oldDamage) {
        this.oldDamage = oldDamage;
    }

    @Generated
    public HurtRenderEvent(boolean oldDamage) {
        this.oldDamage = oldDamage;
    }
}
