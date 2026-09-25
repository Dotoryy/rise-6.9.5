package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C13PacketPlayerAbilities;

public class ScriptPacketAbilities extends ScriptPacket<C13PacketPlayerAbilities> {
    public ScriptPacketAbilities(C13PacketPlayerAbilities packet) {
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

    public void setInvulnerable(boolean invulnerable) {
        this.wrapped.setInvulnerable(invulnerable);
    }

    public void setFlying(boolean flying) {
        this.wrapped.setFlying(flying);
    }

    public void setAllowFlying(boolean allowFlying) {
        this.wrapped.setAllowFlying(allowFlying);
    }

    public void setCreativeMode(boolean creativeMode) {
        this.wrapped.setCreativeMode(creativeMode);
    }
}
