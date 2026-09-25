package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ScriptPacketCustomPayload extends ScriptPacket<C17PacketCustomPayload> {
    public ScriptPacketCustomPayload(C17PacketCustomPayload packet) {
        super(packet);
    }

    public String getChannelName() {
        return this.wrapped.getChannelName();
    }

    public void setChannel(String channel) {
        this.wrapped.setChannel(channel);
    }
}
