package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.input.ChatInputEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;

public class ScriptChatInputEvent extends CancellableScriptEvent<ChatInputEvent> {
    public ScriptChatInputEvent(ChatInputEvent event) {
        super(event);
    }

    public String getMessage() {
        return this.wrapped.getMessage();
    }

    @Override
    public String getHandlerName() {
        return "onChatInput";
    }
}
