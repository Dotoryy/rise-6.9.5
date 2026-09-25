package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S20PacketEntityProperties;

public class ScriptPacketEntityProperties extends ScriptPacket<S20PacketEntityProperties> {
    public ScriptPacketEntityProperties(S20PacketEntityProperties packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }
}
