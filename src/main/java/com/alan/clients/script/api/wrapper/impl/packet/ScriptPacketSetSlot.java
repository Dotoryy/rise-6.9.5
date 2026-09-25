package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.az;

public class ScriptPacketSetSlot extends ScriptPacket<az> {
    public ScriptPacketSetSlot(az var1) {
        super(var1);
    }

    public int getWindowId() {
        return this.wrapped.ahn();
    }

    public int getSlotId() {
        return this.wrapped.aho();
    }
}
