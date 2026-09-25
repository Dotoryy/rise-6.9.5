package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;

public class ScriptPacketLook extends ScriptPacket<C05PacketPlayerLook> {
    public ScriptPacketLook(C05PacketPlayerLook packet) {
        super(packet);
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
