package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptJumpEvent;
import lombok.Generated;

public class JumpEvent extends CancellableEvent {
    private float jumpMotion;
    private float yaw;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptJumpEvent(this);
    }

    @Generated
    public float getJumpMotion() {
        return this.jumpMotion;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public void setJumpMotion(float jumpMotion) {
        this.jumpMotion = jumpMotion;
    }

    @Generated
    public void setYaw(float var1) {
        this.yaw = var1;
    }

    @Generated
    public JumpEvent(float var1, float var2) {
        this.jumpMotion = var1;
        this.yaw = var2;
    }
}
