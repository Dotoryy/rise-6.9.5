package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.cd;

public class ScriptPacketPlayerListHeaderFooter extends ScriptPacket<cd> {
    public ScriptPacketPlayerListHeaderFooter(cd var1) {
        super(var1);
    }

    public String getHeader() {
        return this.wrapped.getHeader() != null ? this.wrapped.getHeader().getUnformattedText() : "";
    }

    public String getFooter() {
        return this.wrapped.getFooter() != null ? this.wrapped.getFooter().getUnformattedText() : "";
    }
}
