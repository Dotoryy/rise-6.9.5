package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.s;

public class ScriptPacketSpawnXPOrb extends ScriptPacket<s> {
    public ScriptPacketSpawnXPOrb(s var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
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

    public int getXPValue() {
        return this.wrapped.getXPValue();
    }
}
