package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class ScriptPacketVelocity extends ScriptPacket<S12PacketEntityVelocity> {
    public ScriptPacketVelocity(S12PacketEntityVelocity packet) {
        super(packet);
    }

    public int getEntityID() {
        return this.wrapped.getEntityID();
    }

    public int getMotionXRaw() {
        return this.wrapped.motionX;
    }

    public int getMotionYRaw() {
        return this.wrapped.motionY;
    }

    public int getMotionZRaw() {
        return this.wrapped.motionZ;
    }

    public double getMotionX() {
        return this.wrapped.motionX / 8000.0;
    }

    public double getMotionY() {
        return this.wrapped.motionY / 8000.0;
    }

    public double getMotionZ() {
        return this.wrapped.motionZ / 8000.0;
    }

    public void setMotionX(double var1) {
        this.wrapped.motionX = (int)(var1 * 8000.0);
    }

    public void setMotionY(double var1) {
        this.wrapped.motionY = (int)(var1 * 8000.0);
    }

    public void setMotionZ(double var1) {
        this.wrapped.motionZ = (int)(var1 * 8000.0);
    }

    public void setMotionXRaw(int var1) {
        this.wrapped.motionX = var1;
    }

    public void setMotionYRaw(int var1) {
        this.wrapped.motionY = var1;
    }

    public void setMotionZRaw(int var1) {
        this.wrapped.motionZ = var1;
    }
}
