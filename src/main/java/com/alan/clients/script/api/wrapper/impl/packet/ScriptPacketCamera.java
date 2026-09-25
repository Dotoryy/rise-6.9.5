package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.bx;

public class ScriptPacketCamera extends ScriptPacket<bx> {
    public ScriptPacketCamera(bx var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntity(MC.theWorld) != null ? this.wrapped.getEntity(MC.theWorld).getEntityId() : -1;
    }
}
