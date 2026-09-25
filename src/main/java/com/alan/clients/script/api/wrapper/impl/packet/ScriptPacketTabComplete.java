package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.client.C14PacketTabComplete;

public class ScriptPacketTabComplete extends ScriptPacket<C14PacketTabComplete> {
    public ScriptPacketTabComplete(C14PacketTabComplete packet) {
        super(packet);
    }

    public String getMessage() {
        return this.wrapped.getMessage();
    }

    public ScriptBlockPos getTargetBlock() {
        return this.wrapped.getTargetBlock() != null ? new ScriptBlockPos(this.wrapped.getTargetBlock()) : null;
    }
}
