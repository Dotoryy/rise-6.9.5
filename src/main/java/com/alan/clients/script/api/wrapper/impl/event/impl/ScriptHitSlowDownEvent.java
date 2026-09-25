package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.motion.HitSlowDownEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptHitSlowDownEvent extends ScriptEvent<HitSlowDownEvent> {
    public ScriptHitSlowDownEvent(HitSlowDownEvent event) {
        super(event);
    }

    public void setSlowDown(double slowDown) {
        this.wrapped.setSlowDown(slowDown);
    }

    public void setSprint(boolean sprint) {
        this.wrapped.setSprint(sprint);
    }

    public double getSlowDown() {
        return this.wrapped.getSlowDown();
    }

    public boolean isSprint() {
        return this.wrapped.isSprint();
    }

    @Override
    public String getHandlerName() {
        return "onHitSlowDown";
    }
}
