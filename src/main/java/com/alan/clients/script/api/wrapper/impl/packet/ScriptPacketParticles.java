package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S2APacketParticles;

public class ScriptPacketParticles extends ScriptPacket<S2APacketParticles> {
    public ScriptPacketParticles(S2APacketParticles packet) {
        super(packet);
    }

    public String getParticleType() {
        return this.wrapped.getParticleType() != null ? this.wrapped.getParticleType().getParticleName() : "";
    }

    public float getX() {
        return (float)this.wrapped.getXCoordinate();
    }

    public float getY() {
        return (float)this.wrapped.getYCoordinate();
    }

    public float getZ() {
        return (float)this.wrapped.getZCoordinate();
    }

    public float getXOffset() {
        return this.wrapped.getXOffset();
    }

    public float getYOffset() {
        return this.wrapped.getYOffset();
    }

    public float getZOffset() {
        return this.wrapped.getZOffset();
    }

    public float getParticleSpeed() {
        return this.wrapped.getParticleSpeed();
    }

    public int getParticleCount() {
        return this.wrapped.getParticleCount();
    }

    public boolean isLongDistance() {
        return this.wrapped.isLongDistance();
    }
}
