package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.l;

public class ScriptPacketUseBed extends ScriptPacket<l> {
    public ScriptPacketUseBed(l var1) {
        super(var1);
    }

    public ScriptBlockPos getBedPosition() {
        return new ScriptBlockPos(this.wrapped.getBedPosition());
    }
}
