package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class ScriptPacketTimeUpdate extends ScriptPacket<S03PacketTimeUpdate> {
    public ScriptPacketTimeUpdate(S03PacketTimeUpdate packet) {
        super(packet);
    }

    public long getTotalWorldTime() {
        return this.wrapped.getTotalWorldTime();
    }

    public long getWorldTime() {
        return this.wrapped.getWorldTime();
    }
}
