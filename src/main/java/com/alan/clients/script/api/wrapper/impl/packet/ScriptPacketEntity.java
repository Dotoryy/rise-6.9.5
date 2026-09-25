package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S14PacketEntity;

public class ScriptPacketEntity extends ScriptPacket<S14PacketEntity> {
    public ScriptPacketEntity(S14PacketEntity packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.getEntity(MC.theWorld) != null ? this.wrapped.getEntity(MC.theWorld).getEntityId() : -1;
    }

    public boolean isOnGround() {
        return this.wrapped.agG();
    }
}
