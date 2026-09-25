package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptMoveInputEvent;
import lombok.Generated;

public class MoveInputEvent implements Event {
    private float forward;
    private float strafe;
    private boolean jump;
    private boolean sneak;
    private double sneakSlowDownMultiplier;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptMoveInputEvent(this);
    }

    @Generated
    public float getForward() {
        return this.forward;
    }

    @Generated
    public float getStrafe() {
        return this.strafe;
    }

    @Generated
    public boolean isJump() {
        return this.jump;
    }

    @Generated
    public boolean isSneak() {
        return this.sneak;
    }

    @Generated
    public double getSneakSlowDownMultiplier() {
        return this.sneakSlowDownMultiplier;
    }

    @Generated
    public void setForward(float forward) {
        this.forward = forward;
    }

    @Generated
    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    @Generated
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    @Generated
    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }

    @Generated
    public void setSneakSlowDownMultiplier(double var1) {
        this.sneakSlowDownMultiplier = var1;
    }

    @Generated
    public MoveInputEvent(float var1, float var2, boolean var3, boolean var4, double var5) {
        this.forward = var1;
        this.strafe = var2;
        this.jump = var3;
        this.sneak = var4;
        this.sneakSlowDownMultiplier = var5;
    }
}
