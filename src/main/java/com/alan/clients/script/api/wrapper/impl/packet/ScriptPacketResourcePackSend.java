package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S48PacketResourcePackSend;

public class ScriptPacketResourcePackSend extends ScriptPacket<S48PacketResourcePackSend> {
    public ScriptPacketResourcePackSend(S48PacketResourcePackSend packet) {
        super(packet);
    }

    public String getURL() {
        return this.wrapped.getURL();
    }

    public String getHash() {
        return this.wrapped.getHash();
    }
}
