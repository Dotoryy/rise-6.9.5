package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.k;

public class ScriptPacketServerHeldItem extends ScriptPacket<k> {
    public ScriptPacketServerHeldItem(k var1) {
        super(var1);
    }

    public int getHeldItemHotbarIndex() {
        return this.wrapped.getHeldItemHotbarIndex();
    }
}
