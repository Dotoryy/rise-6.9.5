package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptPreMotionEvent;
import lombok.Generated;

public final class PreMotionEvent extends CancellableEvent {
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean sprinting;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptPreMotionEvent(this);
    }

    @Generated
    public double getPosX() {
        return this.posX;
    }

    @Generated
    public double getPosY() {
        return this.posY;
    }

    @Generated
    public double getPosZ() {
        return this.posZ;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public boolean isOnGround() {
        return this.onGround;
    }

    @Generated
    public boolean isSprinting() {
        return this.sprinting;
    }

    @Generated
    public void setPosX(double var1) {
        this.posX = var1;
    }

    @Generated
    public void setPosY(double var1) {
        this.posY = var1;
    }

    @Generated
    public void setPosZ(double var1) {
        this.posZ = var1;
    }

    @Generated
    public void setYaw(float var1) {
        this.yaw = var1;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Generated
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Generated
    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    @Generated
    public PreMotionEvent(double var1, double var3, double var5, float var7, float var8, boolean var9, boolean var10) {
        this.posX = var1;
        this.posY = var3;
        this.posZ = var5;
        this.yaw = var7;
        this.pitch = var8;
        this.onGround = var9;
        this.sprinting = var10;
    }
}
