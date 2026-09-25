package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.server.bg;

public class ScriptPacketSignEditorOpen extends ScriptPacket<bg> {
    public ScriptPacketSignEditorOpen(bg var1) {
        super(var1);
    }

    public ScriptBlockPos getSignPosition() {
        return new ScriptBlockPos(this.wrapped.getSignPosition());
    }
}
