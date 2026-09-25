package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;

public class ScriptPacketSpawnGlobalEntity extends ScriptPacket<S2CPacketSpawnGlobalEntity> {
    public ScriptPacketSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.func_149052_c();
    }

    public double getX() {
        return this.wrapped.func_149051_d() / 32.0;
    }

    public double getY() {
        return this.wrapped.func_149050_e() / 32.0;
    }

    public double getZ() {
        return this.wrapped.func_149049_f() / 32.0;
    }

    public int getEntityType() {
        return this.wrapped.func_149053_g();
    }
}
