package com.alan.clients.newevent.impl.input;

import com.alan.clients.Client;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptChatInputEvent;
import lombok.Generated;

public final class ChatInputEvent extends CancellableEvent {
    private String message;

    public static void p(String var0) {
        Client.a.e().d(new ChatInputEvent(var0));
    }

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptChatInputEvent(this);
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public ChatInputEvent(String message) {
        this.message = message;
    }
}
