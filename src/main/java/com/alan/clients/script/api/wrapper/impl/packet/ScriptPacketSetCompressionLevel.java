package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.cc;

public class ScriptPacketSetCompressionLevel extends ScriptPacket<cc> {
    public ScriptPacketSetCompressionLevel(cc var1) {
        super(var1);
    }

    public int getThreshold() {
        return this.wrapped.getThreshold();
    }
}
