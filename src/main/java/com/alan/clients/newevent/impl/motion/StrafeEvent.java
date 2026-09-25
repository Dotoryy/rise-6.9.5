package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptStrafeEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.player.MoveUtil;
import lombok.Generated;

public final class StrafeEvent extends CancellableEvent implements InstanceAccess {
    private float forward;
    private float strafe;
    private float friction;
    private float yaw;

    public void setSpeed(double var1, double var3) {
        this.setFriction((float)(this.getForward() != 0.0F && this.getStrafe() != 0.0F ? var1 * 0.98F : var1));
        aEg.thePlayer.motionX *= var3;
        aEg.thePlayer.motionZ *= var3;
    }

    public void setSpeed(double speed) {
        this.setFriction((float)(this.getForward() != 0.0F && this.getStrafe() != 0.0F ? speed * 0.98F : speed));
        MoveUtil.stop();
    }

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptStrafeEvent(this);
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
    public float getFriction() {
        return this.friction;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
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
    public void setFriction(float friction) {
        this.friction = friction;
    }

    @Generated
    public void setYaw(float var1) {
        this.yaw = var1;
    }

    @Generated
    public StrafeEvent(float var1, float var2, float var3, float var4) {
        this.forward = var1;
        this.strafe = var2;
        this.friction = var3;
        this.yaw = var4;
    }
}
