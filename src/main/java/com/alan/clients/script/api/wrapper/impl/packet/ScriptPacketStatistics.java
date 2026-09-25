package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S37PacketStatistics;

public class ScriptPacketStatistics extends ScriptPacket<S37PacketStatistics> {
    public ScriptPacketStatistics(S37PacketStatistics packet) {
        super(packet);
    }

    public int getEntryCount() {
        return this.wrapped.func_148974_c() != null ? this.wrapped.func_148974_c().size() : 0;
    }
}
