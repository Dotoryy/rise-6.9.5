package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S3CPacketUpdateScore;

public class ScriptPacketUpdateScore extends ScriptPacket<S3CPacketUpdateScore> {
    public ScriptPacketUpdateScore(S3CPacketUpdateScore packet) {
        super(packet);
    }

    public String getPlayerName() {
        return this.wrapped.getPlayerName();
    }

    public String getObjectiveName() {
        return this.wrapped.getObjectiveName();
    }

    public int getScoreValue() {
        return this.wrapped.getScoreValue();
    }

    public String getAction() {
        return this.wrapped.getScoreAction() != null ? this.wrapped.getScoreAction().name() : "";
    }
}
