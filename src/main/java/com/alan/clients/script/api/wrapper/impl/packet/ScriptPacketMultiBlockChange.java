package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S22PacketMultiBlockChange;

public class ScriptPacketMultiBlockChange extends ScriptPacket<S22PacketMultiBlockChange> {
    public ScriptPacketMultiBlockChange(S22PacketMultiBlockChange packet) {
        super(packet);
    }

    public int getChangedBlockCount() {
        return this.wrapped.getChangedBlocks() != null ? this.wrapped.getChangedBlocks().length : 0;
    }
}
