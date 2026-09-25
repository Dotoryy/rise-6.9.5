package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.c;

public class ScriptPacketChat extends ScriptPacket<c> {
    public ScriptPacketChat(c var1) {
        super(var1);
    }

    public String getText() {
        return this.wrapped.getChatComponent().getUnformattedText();
    }

    public String getFormattedText() {
        return this.wrapped.getChatComponent().getFormattedText();
    }

    public byte getChatType() {
        return this.wrapped.getType();
    }

    public boolean isChat() {
        return this.wrapped.isChat();
    }
}
