package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S13PacketDestroyEntities;

public class ScriptPacketDestroyEntities extends ScriptPacket<S13PacketDestroyEntities> {
    public ScriptPacketDestroyEntities(S13PacketDestroyEntities packet) {
        super(packet);
    }

    public int getEntityCount() {
        return this.wrapped.getEntityIDs() != null ? this.wrapped.getEntityIDs().length : 0;
    }

    public int getEntityId(int var1) {
        int[] aint = this.wrapped.getEntityIDs();
        return aint != null && var1 >= 0 && var1 < aint.length ? aint[var1] : -1;
    }
}
