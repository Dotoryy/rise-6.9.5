package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.S23PacketBlockChange;

public class ScriptPacketBlockChange extends ScriptPacket<S23PacketBlockChange> {
    public ScriptPacketBlockChange(S23PacketBlockChange packet) {
        super(packet);
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.getBlockPosition());
    }
}
