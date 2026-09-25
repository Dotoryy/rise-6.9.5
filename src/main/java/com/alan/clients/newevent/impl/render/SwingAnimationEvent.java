package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;

public final class SwingAnimationEvent extends CancellableEvent {
    private int animationEnd;

    @Generated
    public int dK() {
        return this.animationEnd;
    }

    @Generated
    public void setAnimationEnd(int animationEnd) {
        this.animationEnd = animationEnd;
    }

    @Generated
    public SwingAnimationEvent(int animationEnd) {
        this.animationEnd = animationEnd;
    }
}
