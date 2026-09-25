package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.t;

public class ScriptPacketCreativeAction extends ScriptPacket<t> {
    public ScriptPacketCreativeAction(t var1) {
        super(var1);
    }

    public int getSlotId() {
        return this.wrapped.getSlotId();
    }
}
