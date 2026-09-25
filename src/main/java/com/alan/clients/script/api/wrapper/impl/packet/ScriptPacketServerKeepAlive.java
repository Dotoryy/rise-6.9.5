package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.a;

public class ScriptPacketServerKeepAlive extends ScriptPacket<a> {
    public ScriptPacketServerKeepAlive(a var1) {
        super(var1);
    }

    public int getId() {
        return this.wrapped.func_149134_c();
    }
}
