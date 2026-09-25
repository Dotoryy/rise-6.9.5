package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.ac;

public class ScriptPacketEntityAttach extends ScriptPacket<ac> {
    public ScriptPacketEntityAttach(ac var1) {
        super(var1);
    }

    public int getLeash() {
        return this.wrapped.getLeash();
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public int getVehicleEntityId() {
        return this.wrapped.getVehicleEntityId();
    }
}
