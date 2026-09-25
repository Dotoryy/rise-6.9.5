package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.ad;

public class ScriptPacketEntityMetadata extends ScriptPacket<ad> {
    public ScriptPacketEntityMetadata(ad var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }
}
