package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;

public final class ViewBobbingEvent extends CancellableEvent {
    private int time;

    @Generated
    public int getTime() {
        return this.time;
    }

    @Generated
    public void setTime(int time) {
        this.time = time;
    }

    @Generated
    public ViewBobbingEvent(int time) {
        this.time = time;
    }
}
