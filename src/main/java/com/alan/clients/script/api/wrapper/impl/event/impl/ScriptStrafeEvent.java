package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;

public class ScriptStrafeEvent extends CancellableScriptEvent<StrafeEvent> {
    public ScriptStrafeEvent(StrafeEvent event) {
        super(event);
    }

    public void setForward(float forward) {
        this.wrapped.setForward(forward);
    }

    public void setStrafe(float strafe) {
        this.wrapped.setStrafe(strafe);
    }

    public void setFriction(float friction) {
        this.wrapped.setFriction(friction);
    }

    public void setYaw(float var1) {
        this.wrapped.setYaw(var1);
    }

    public float getForward() {
        return this.wrapped.getForward();
    }

    public float getStrafe() {
        return this.wrapped.getStrafe();
    }

    public float getFriction() {
        return this.wrapped.getFriction();
    }

    public float getYaw() {
        return this.wrapped.getYaw();
    }

    public void setSpeed(double var1, double var3) {
        this.wrapped.setSpeed(var1, var3);
    }

    public void setSpeed(double speed) {
        this.wrapped.setSpeed(speed);
    }

    @Override
    public String getHandlerName() {
        return "onStrafe";
    }
}
