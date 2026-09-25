package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.a;

public class ScriptPacketKeepAlive extends ScriptPacket<a> {
    public ScriptPacketKeepAlive(a var1) {
        super(var1);
    }

    public int getKey() {
        return this.wrapped.getKey();
    }

    public void setKey(int var1) {
        this.wrapped.key = var1;
    }
}
