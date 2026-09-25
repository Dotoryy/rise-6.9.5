package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.S10PacketSpawnPainting;

public class ScriptPacketSpawnPainting extends ScriptPacket<S10PacketSpawnPainting> {
    public ScriptPacketSpawnPainting(S10PacketSpawnPainting packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.getPosition());
    }

    public int getFacing() {
        return this.wrapped.getFacing().getHorizontalIndex();
    }

    public String getTitle() {
        return this.wrapped.getTitle();
    }
}
