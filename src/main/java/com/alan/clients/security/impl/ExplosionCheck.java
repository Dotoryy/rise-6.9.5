package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class ExplosionCheck extends CrashCheck {
    public ExplosionCheck() {
        super("Explosion Checker", "Server attempted to crash the client with a large explosion");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        return !(packet instanceof S27PacketExplosion s27packetexplosion)
            ? false
            : s27packetexplosion.func_149149_c() >= 127.0F || s27packetexplosion.func_149144_d() >= 127.0F || s27packetexplosion.func_149147_e() >= 127.0F;
    }
}
