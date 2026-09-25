package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S01PacketJoinGame;

public class ScriptPacketJoinGame extends ScriptPacket<S01PacketJoinGame> {
    public ScriptPacketJoinGame(S01PacketJoinGame packet) {
        super(packet);
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public boolean isHardcoreMode() {
        return this.wrapped.isHardcoreMode();
    }

    public String getGameType() {
        return this.wrapped.getGameType().getName();
    }

    public int getDimension() {
        return this.wrapped.getDimension();
    }

    public String getDifficulty() {
        return this.wrapped.getDifficulty().getDifficultyResourceKey();
    }

    public int getMaxPlayers() {
        return this.wrapped.getMaxPlayers();
    }

    public boolean isReducedDebugInfo() {
        return this.wrapped.isReducedDebugInfo();
    }
}
