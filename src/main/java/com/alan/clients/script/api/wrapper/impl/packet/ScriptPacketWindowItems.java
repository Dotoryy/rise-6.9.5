package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S30PacketWindowItems;

public class ScriptPacketWindowItems extends ScriptPacket<S30PacketWindowItems> {
    public ScriptPacketWindowItems(S30PacketWindowItems packet) {
        super(packet);
    }

    public int getWindowId() {
        return this.wrapped.func_148911_c();
    }

    public int getItemCount() {
        return this.wrapped.getItemStacks() != null ? this.wrapped.getItemStacks().length : 0;
    }
}
