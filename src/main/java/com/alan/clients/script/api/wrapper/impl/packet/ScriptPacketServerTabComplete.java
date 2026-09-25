package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S3APacketTabComplete;

public class ScriptPacketServerTabComplete extends ScriptPacket<S3APacketTabComplete> {
    public ScriptPacketServerTabComplete(S3APacketTabComplete packet) {
        super(packet);
    }

    public int getMatchCount() {
        return this.wrapped.func_149630_c() != null ? this.wrapped.func_149630_c().length : 0;
    }

    public String getMatch(int var1) {
        String[] astring = this.wrapped.func_149630_c();
        return astring != null && var1 >= 0 && var1 < astring.length ? astring[var1] : "";
    }
}
