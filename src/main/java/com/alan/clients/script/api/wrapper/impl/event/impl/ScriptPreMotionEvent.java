package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;

public class ScriptPreMotionEvent extends CancellableScriptEvent<PreMotionEvent> {
    public ScriptPreMotionEvent(PreMotionEvent event) {
        super(event);
    }

    public void setPosX(double var1) {
        this.wrapped.setPosX(var1);
    }

    public void setPosY(double var1) {
        this.wrapped.setPosY(var1);
    }

    public void setPosZ(double var1) {
        this.wrapped.setPosZ(var1);
    }

    public void setYaw(float var1) {
        this.wrapped.setYaw(var1);
    }

    public void setPitch(float pitch) {
        this.wrapped.setPitch(pitch);
    }

    public void setOnGround(boolean onGround) {
        this.wrapped.setOnGround(onGround);
    }

    public void setSprinting(boolean sprinting) {
        this.wrapped.setSprinting(sprinting);
    }

    public double getPosX() {
        return this.wrapped.getPosX();
    }

    public double getPosY() {
        return this.wrapped.getPosY();
    }

    public double getPosZ() {
        return this.wrapped.getPosZ();
    }

    public float getYaw() {
        return this.wrapped.getYaw();
    }

    public float getPitch() {
        return this.wrapped.getPitch();
    }

    public boolean isOnGround() {
        return this.wrapped.isOnGround();
    }

    public boolean isSprinting() {
        return this.wrapped.isSprinting();
    }

    @Override
    public String getHandlerName() {
        return "onPreMotion";
    }
}
