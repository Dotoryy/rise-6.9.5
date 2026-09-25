package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C03PacketPlayer;

public class ScriptPacketPlayer extends ScriptPacket<C03PacketPlayer> {
    public ScriptPacketPlayer(C03PacketPlayer packet) {
        super(packet);
    }

    public boolean isOnGround() {
        return this.wrapped.isOnGround();
    }

    public boolean isMoving() {
        return this.wrapped.isMoving();
    }

    public boolean isRotating() {
        return this.wrapped.afG();
    }

    public void setOnGround(boolean onGround) {
        this.wrapped.setOnGround(onGround);
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
}
