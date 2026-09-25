package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class ScriptPacketDigging extends ScriptPacket<C07PacketPlayerDigging> {
    public ScriptPacketDigging(C07PacketPlayerDigging packet) {
        super(packet);
    }

    public ScriptBlockPos getPosition() {
        return new ScriptBlockPos(this.wrapped.getPosition());
    }

    public int getFacing() {
        return this.wrapped.getFacing().getIndex();
    }

    public String getStatus() {
        return this.wrapped.getStatus().name();
    }
}
