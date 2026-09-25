package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;

public class ScriptMoveEvent extends CancellableScriptEvent<MoveEvent> {
    public ScriptMoveEvent(MoveEvent event) {
        super(event);
    }

    public void setX(double var1) {
        this.wrapped.setPosX(var1);
    }

    public void setY(double var1) {
        this.wrapped.setPosY(var1);
    }

    public void setZ(double var1) {
        this.wrapped.setPosZ(var1);
    }

    public double getX() {
        return this.wrapped.getPosX();
    }

    public double getY() {
        return this.wrapped.getPosY();
    }

    public double getZ() {
        return this.wrapped.getPosZ();
    }

    @Override
    public String getHandlerName() {
        return "onMove";
    }
}
