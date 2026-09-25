package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C16PacketClientStatus;

public class ScriptPacketClientStatus extends ScriptPacket<C16PacketClientStatus> {
    public ScriptPacketClientStatus(C16PacketClientStatus packet) {
        super(packet);
    }

    public String getStatus() {
        return this.wrapped.getStatus().name();
    }
}
