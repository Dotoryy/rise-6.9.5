package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class ScriptPacketPosition extends ScriptPacket<C04PacketPlayerPosition> {
    public ScriptPacketPosition(C04PacketPlayerPosition packet) {
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

    public void setOnGround(boolean onGround) {
        this.wrapped.setOnGround(onGround);
    }
}
