package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S45PacketTitle;

public class ScriptPacketTitle extends ScriptPacket<S45PacketTitle> {
    public ScriptPacketTitle(S45PacketTitle packet) {
        super(packet);
    }

    @Override
    public String getType() {
        return this.wrapped.getType() != null ? this.wrapped.getType().name() : "";
    }

    public String getMessage() {
        return this.wrapped.getMessage() != null ? this.wrapped.getMessage().getUnformattedText() : "";
    }

    public int getFadeInTime() {
        return this.wrapped.getFadeInTime();
    }

    public int getDisplayTime() {
        return this.wrapped.getDisplayTime();
    }

    public int getFadeOutTime() {
        return this.wrapped.getFadeOutTime();
    }
}
