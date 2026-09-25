package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C0CPacketInput;

public class ScriptPacketInput extends ScriptPacket<C0CPacketInput> {
    public ScriptPacketInput(C0CPacketInput packet) {
        super(packet);
    }

    public float getStrafeSpeed() {
        return this.wrapped.getStrafeSpeed();
    }

    public float getForwardSpeed() {
        return this.wrapped.getForwardSpeed();
    }

    public boolean isJumping() {
        return this.wrapped.isJumping();
    }

    public boolean isSneaking() {
        return this.wrapped.isSneaking();
    }
}
