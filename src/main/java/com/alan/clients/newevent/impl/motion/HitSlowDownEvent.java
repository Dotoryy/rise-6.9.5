package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptHitSlowDownEvent;
import lombok.Generated;

public final class HitSlowDownEvent extends CancellableEvent {
    public double slowDown;
    public boolean sprint;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptHitSlowDownEvent(this);
    }

    @Generated
    public double getSlowDown() {
        return this.slowDown;
    }

    @Generated
    public boolean isSprint() {
        return this.sprint;
    }

    @Generated
    public void setSlowDown(double slowDown) {
        this.slowDown = slowDown;
    }

    @Generated
    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    @Generated
    public HitSlowDownEvent(double slowDown, boolean sprint) {
        this.slowDown = slowDown;
        this.sprint = sprint;
    }
}
