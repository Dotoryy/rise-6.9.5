package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S38PacketPlayerListItem;

public class ScriptPacketPlayerListItem extends ScriptPacket<S38PacketPlayerListItem> {
    public ScriptPacketPlayerListItem(S38PacketPlayerListItem packet) {
        super(packet);
    }

    public String getAction() {
        return this.wrapped.getAction() != null ? this.wrapped.getAction().name() : "";
    }

    public int getEntryCount() {
        return this.wrapped.getEntries() != null ? this.wrapped.getEntries().size() : 0;
    }
}
