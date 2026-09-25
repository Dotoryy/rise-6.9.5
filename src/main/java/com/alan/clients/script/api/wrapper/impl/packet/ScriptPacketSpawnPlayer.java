package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.n;

public class ScriptPacketSpawnPlayer extends ScriptPacket<n> {
    public ScriptPacketSpawnPlayer(n var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public String getPlayerUUID() {
        return this.wrapped.agh() != null ? this.wrapped.agh().toString() : "";
    }

    public double getX() {
        return this.wrapped.we() / 32.0;
    }

    public double getY() {
        return this.wrapped.wf() / 32.0;
    }

    public double getZ() {
        return this.wrapped.wi() / 32.0;
    }

    public float getYaw() {
        return this.wrapped.agi() * 360.0F / 256.0F;
    }

    public float getPitch() {
        return this.wrapped.agj() * 360.0F / 256.0F;
    }
}
