package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S06PacketUpdateHealth;

public class ScriptPacketUpdateHealth extends ScriptPacket<S06PacketUpdateHealth> {
    public ScriptPacketUpdateHealth(S06PacketUpdateHealth packet) {
        super(packet);
    }

    public float getHealth() {
        return this.wrapped.getHealth();
    }

    public int getFoodLevel() {
        return this.wrapped.getFoodLevel();
    }

    public float getSaturationLevel() {
        return this.wrapped.getSaturationLevel();
    }
}
