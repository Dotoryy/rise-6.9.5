package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S39PacketPlayerAbilities;

public class ScriptPacketServerAbilities extends ScriptPacket<S39PacketPlayerAbilities> {
    public ScriptPacketServerAbilities(S39PacketPlayerAbilities packet) {
        super(packet);
    }

    public boolean isInvulnerable() {
        return this.wrapped.isInvulnerable();
    }

    public boolean isFlying() {
        return this.wrapped.isFlying();
    }

    public boolean isAllowFlying() {
        return this.wrapped.isAllowFlying();
    }

    public boolean isCreativeMode() {
        return this.wrapped.isCreativeMode();
    }

    public float getFlySpeed() {
        return this.wrapped.getFlySpeed();
    }

    public float getWalkSpeed() {
        return this.wrapped.getWalkSpeed();
    }
}
