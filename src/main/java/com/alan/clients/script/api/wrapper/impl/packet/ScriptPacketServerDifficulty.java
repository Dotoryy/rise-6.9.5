package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S41PacketServerDifficulty;

public class ScriptPacketServerDifficulty extends ScriptPacket<S41PacketServerDifficulty> {
    public ScriptPacketServerDifficulty(S41PacketServerDifficulty packet) {
        super(packet);
    }

    public String getDifficulty() {
        return this.wrapped.getDifficulty() != null ? this.wrapped.getDifficulty().getDifficultyResourceKey() : "";
    }

    public boolean isDifficultyLocked() {
        return this.wrapped.isDifficultyLocked();
    }
}
