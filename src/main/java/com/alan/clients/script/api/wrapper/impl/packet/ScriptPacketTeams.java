package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S3EPacketTeams;

public class ScriptPacketTeams extends ScriptPacket<S3EPacketTeams> {
    public ScriptPacketTeams(S3EPacketTeams packet) {
        super(packet);
    }

    public String getName() {
        return this.wrapped.getName();
    }

    public String getDisplayName() {
        return this.wrapped.ahP();
    }

    public String getPrefix() {
        return this.wrapped.getPrefix();
    }

    public String getSuffix() {
        return this.wrapped.getSuffix();
    }

    public int getAction() {
        return this.wrapped.ahT();
    }

    public int getPlayerCount() {
        return this.wrapped.getPlayers() != null ? this.wrapped.getPlayers().size() : 0;
    }

    public int getFriendlyFireMode() {
        return this.wrapped.getFriendlyFlags();
    }
}
