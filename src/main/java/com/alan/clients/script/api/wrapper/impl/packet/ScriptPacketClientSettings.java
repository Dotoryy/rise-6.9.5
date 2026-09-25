package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C15PacketClientSettings;

public class ScriptPacketClientSettings extends ScriptPacket<C15PacketClientSettings> {
    public ScriptPacketClientSettings(C15PacketClientSettings packet) {
        super(packet);
    }

    public String getLanguage() {
        return this.wrapped.getLang();
    }

    public String getChatVisibility() {
        return this.wrapped.getChatVisibility().name();
    }

    public boolean isColorsEnabled() {
        return this.wrapped.isColorsEnabled();
    }

    public int getModelPartFlags() {
        return this.wrapped.getModelPartFlags();
    }
}
