package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S31PacketWindowProperty;

public class ScriptPacketWindowProperty extends ScriptPacket<S31PacketWindowProperty> {
    public ScriptPacketWindowProperty(S31PacketWindowProperty packet) {
        super(packet);
    }

    public int getWindowId() {
        return this.wrapped.getWindowId();
    }

    public int getPropertyIndex() {
        return this.wrapped.getVarIndex();
    }

    public int getPropertyValue() {
        return this.wrapped.getVarValue();
    }
}
