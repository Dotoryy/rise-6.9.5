package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class ScriptPacketPlayerPosLook extends ScriptPacket<S08PacketPlayerPosLook> {
    public ScriptPacketPlayerPosLook(S08PacketPlayerPosLook packet) {
        super(packet);
    }

    public double getX() {
        return this.wrapped.getX();
    }

    public double getY() {
        return this.wrapped.getY();
    }

    public double getZ() {
        return this.wrapped.getZ();
    }

    public float getYaw() {
        return this.wrapped.getYaw();
    }

    public float getPitch() {
        return this.wrapped.getPitch();
    }

    public void setX(double var1) {
        this.wrapped.x = var1;
    }

    public void setY(double var1) {
        this.wrapped.y = var1;
    }

    public void setZ(double var1) {
        this.wrapped.z = var1;
    }

    public void setYaw(float var1) {
        this.wrapped.yaw = var1;
    }

    public void setPitch(float pitch) {
        this.wrapped.pitch = pitch;
    }
}
