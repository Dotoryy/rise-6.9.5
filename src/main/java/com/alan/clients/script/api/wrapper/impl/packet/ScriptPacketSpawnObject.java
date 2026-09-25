package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S0EPacketSpawnObject;

public class ScriptPacketSpawnObject extends ScriptPacket<S0EPacketSpawnObject> {
    public ScriptPacketSpawnObject(S0EPacketSpawnObject packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public int getObjectType() {
        return this.wrapped.getType();
    }

    public double getX() {
        return this.wrapped.getX() / 32.0;
    }

    public double getY() {
        return this.wrapped.getY() / 32.0;
    }

    public double getZ() {
        return this.wrapped.getZ() / 32.0;
    }

    public double getSpeedX() {
        return this.wrapped.getSpeedX() / 8000.0;
    }

    public double getSpeedY() {
        return this.wrapped.getSpeedY() / 8000.0;
    }

    public double getSpeedZ() {
        return this.wrapped.getSpeedZ() / 8000.0;
    }
}
