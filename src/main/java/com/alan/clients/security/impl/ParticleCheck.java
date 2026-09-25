package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;

public final class ParticleCheck extends CrashCheck {
    private int particles;

    public ParticleCheck() {
        super("ParticleCheck", "Server attempted to crash the client with a large amount of particles");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        if (!(packet instanceof S2APacketParticles s2apacketparticles)) {
            return false;
        }
        this.particles = this.particles + s2apacketparticles.getParticleCount();
        this.particles -= 6;
        this.particles = Math.min(this.particles, 150);
        return this.particles > 100
            || s2apacketparticles.getParticleCount() < 1
            || Math.abs(s2apacketparticles.getParticleCount()) > 20
            || s2apacketparticles.getParticleSpeed() < 0.0F
            || s2apacketparticles.getParticleSpeed() > 1000.0F;
    }
}
