package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.Event;
import lombok.Generated;

public final class StepEvent implements Event {
    private double height;

    @Generated
    public double getHeight() {
        return this.height;
    }

    @Generated
    public StepEvent(double height) {
        this.height = height;
    }
}
