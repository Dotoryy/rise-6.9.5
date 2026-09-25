package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.ae;

public class ScriptPacketEntityEffect extends ScriptPacket<ae> {
    public ScriptPacketEntityEffect(ae var1) {
        super(var1);
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public byte getEffectId() {
        return this.wrapped.getEffectId();
    }

    public byte getAmplifier() {
        return this.wrapped.getAmplifier();
    }

    public int getDuration() {
        return this.wrapped.getDuration();
    }

    public boolean isAmbient() {
        return this.wrapped.agK();
    }
}
