package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.f;

public class ScriptPacketSpawnPosition extends ScriptPacket<f> {
    public ScriptPacketSpawnPosition(f var1) {
        super(var1);
    }

    public ScriptBlockPos getSpawnPosition() {
        return new ScriptBlockPos(this.wrapped.getSpawnPos());
    }
}
