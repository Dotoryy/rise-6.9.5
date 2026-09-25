package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C01PacketChatMessage;

public class ScriptPacketChatMessage extends ScriptPacket<C01PacketChatMessage> {
    public ScriptPacketChatMessage(C01PacketChatMessage packet) {
        super(packet);
    }

    public String getMessage() {
        return this.wrapped.getMessage();
    }
}
