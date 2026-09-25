package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.bt;

public class ScriptPacketDisconnect extends ScriptPacket<bt> {
    public ScriptPacketDisconnect(bt var1) {
        super(var1);
    }

    public String getReason() {
        return this.wrapped.getReason() != null ? this.wrapped.getReason().getUnformattedText() : "";
    }
}
