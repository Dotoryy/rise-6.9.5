package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptMoveEvent;
import lombok.Generated;

public final class MoveEvent extends CancellableEvent {
    private double posX;
    private double posY;
    private double posZ;

    @Override
    public ScriptEvent<?> getScriptEvent() {
        return new ScriptMoveEvent(this);
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
    public MoveEvent(double var1, double var3, double var5) {
        this.posX = var1;
        this.posY = var3;
        this.posZ = var5;
    }
}
