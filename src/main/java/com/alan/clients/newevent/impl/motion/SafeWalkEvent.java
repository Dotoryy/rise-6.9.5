package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.Event;
import lombok.Generated;

public final class SafeWalkEvent implements Event {
    private double jy;

    @Generated
    public double da() {
        return this.jy;
    }

    @Generated
    public void h(double var1) {
        this.jy = var1;
    }

    @Generated
    public SafeWalkEvent(double var1) {
        this.jy = var1;
    }
}
