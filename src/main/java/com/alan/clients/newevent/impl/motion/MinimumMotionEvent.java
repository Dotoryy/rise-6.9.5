package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;

public class MinimumMotionEvent extends CancellableEvent {
    private double minimumMotion;

    @Generated
    public double cY() {
        return this.minimumMotion;
    }

    @Generated
    public void setMinimumMotion(double minimumMotion) {
        this.minimumMotion = minimumMotion;
    }

    @Generated
    public MinimumMotionEvent(double minimumMotion) {
        this.minimumMotion = minimumMotion;
    }
}
