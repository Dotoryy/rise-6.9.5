package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S26PacketMapChunkBulk;

public class ScriptPacketMapChunkBulk extends ScriptPacket<S26PacketMapChunkBulk> {
    public ScriptPacketMapChunkBulk(S26PacketMapChunkBulk packet) {
        super(packet);
    }

    public int getChunkCount() {
        return this.wrapped.getChunkCount();
    }
}
