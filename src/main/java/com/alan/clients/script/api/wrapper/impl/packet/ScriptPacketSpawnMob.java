package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.q;

public class ScriptPacketSpawnMob extends ScriptPacket<q> {
    public ScriptPacketSpawnMob(q var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public int getEntityType() {
        return this.wrapped.getEntityType();
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

    public double getVelocityX() {
        return this.wrapped.agu() / 8000.0;
    }

    public double getVelocityY() {
        return this.wrapped.agv() / 8000.0;
    }

    public double getVelocityZ() {
        return this.wrapped.agw() / 8000.0;
    }

    public float getYaw() {
        return this.wrapped.agi() * 360.0F / 256.0F;
    }

    public float getPitch() {
        return this.wrapped.agj() * 360.0F / 256.0F;
    }

    public float getHeadPitch() {
        return this.wrapped.agx() * 360.0F / 256.0F;
    }
}
