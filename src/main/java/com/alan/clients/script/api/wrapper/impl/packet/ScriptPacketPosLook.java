package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class ScriptPacketPosLook extends ScriptPacket<C06PacketPlayerPosLook> {
    public ScriptPacketPosLook(C06PacketPlayerPosLook packet) {
        super(packet);
    }

    public double getX() {
        return this.wrapped.afD();
    }

    public double getY() {
        return this.wrapped.afE();
    }

    public double getZ() {
        return this.wrapped.afF();
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

    public void setOnGround(boolean onGround) {
        this.wrapped.setOnGround(onGround);
    }
}
