package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.af;

public class ScriptPacketRemoveEntityEffect extends ScriptPacket<af> {
    public ScriptPacketRemoveEntityEffect(af var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public int getEffectId() {
        return this.wrapped.getEffectId();
    }
}
