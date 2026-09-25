package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptMoveInputEvent extends ScriptEvent<MoveInputEvent> {
    public ScriptMoveInputEvent(MoveInputEvent event) {
        super(event);
    }

    public void setForward(float forward) {
        this.wrapped.setForward(forward);
    }

    public void setStrafe(float strafe) {
        this.wrapped.setStrafe(strafe);
    }

    public void setJump(boolean jump) {
        this.wrapped.setJump(jump);
    }

    public void setSneak(boolean sneak) {
        this.wrapped.setSneak(sneak);
    }

    public void setSneakSlowDownMultiplier(double var1) {
        this.wrapped.setSneakSlowDownMultiplier(var1);
    }

    public float getForward() {
        return this.wrapped.getForward();
    }

    public float getStrafe() {
        return this.wrapped.getStrafe();
    }

    public boolean isJump() {
        return this.wrapped.isJump();
    }

    public boolean isSneak() {
        return this.wrapped.isSneak();
    }

    public double getSneakSlowDownMultiplier() {
        return this.wrapped.getSneakSlowDownMultiplier();
    }

    @Override
    public String getHandlerName() {
        return "onMoveInput";
    }
}
