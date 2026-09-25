package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S21PacketChunkData;

public class ScriptPacketChunkData extends ScriptPacket<S21PacketChunkData> {
    public ScriptPacketChunkData(S21PacketChunkData packet) {
        super(packet);
    }

    public int getChunkX() {
        return this.wrapped.getChunkX();
    }

    public int getChunkZ() {
        return this.wrapped.getChunkZ();
    }

    public boolean isFullChunk() {
        return this.wrapped.func_149274_i();
    }
}
