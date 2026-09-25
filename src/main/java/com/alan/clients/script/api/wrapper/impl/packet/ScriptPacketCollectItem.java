package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.o;

public class ScriptPacketCollectItem extends ScriptPacket<o> {
    public ScriptPacketCollectItem(o var1) {
        super(var1);
    }

    public int getCollectedItemEntityId() {
        return this.wrapped.getCollectedItemEntityID();
    }

    public int getCollectorEntityId() {
        return this.wrapped.getEntityID();
    }
}
