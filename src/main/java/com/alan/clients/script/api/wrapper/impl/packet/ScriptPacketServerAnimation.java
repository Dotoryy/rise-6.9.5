package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.m;

public class ScriptPacketServerAnimation extends ScriptPacket<m> {
    public ScriptPacketServerAnimation(m var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityID();
    }

    public int getAnimationType() {
        return this.wrapped.getAnimationType();
    }
}
