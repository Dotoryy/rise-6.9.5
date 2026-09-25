package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C02PacketUseEntity;

public class ScriptPacketUseEntity extends ScriptPacket<C02PacketUseEntity> {
    public ScriptPacketUseEntity(C02PacketUseEntity packet) {
        super(packet);
    }

    public String getAction() {
        return this.wrapped.getAction().name();
    }

    public double getHitVecX() {
        return this.wrapped.getHitVec() != null ? this.wrapped.getHitVec().xCoord : 0.0;
    }

    public double getHitVecY() {
        return this.wrapped.getHitVec() != null ? this.wrapped.getHitVec().yCoord : 0.0;
    }

    public double getHitVecZ() {
        return this.wrapped.getHitVec() != null ? this.wrapped.getHitVec().zCoord : 0.0;
    }
}
