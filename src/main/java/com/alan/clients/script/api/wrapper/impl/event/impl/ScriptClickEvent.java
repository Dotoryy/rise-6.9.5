package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptClickEvent extends ScriptEvent<ClickEvent> {
    public ScriptClickEvent(ClickEvent event) {
        super(event);
    }

    @Override
    public String getHandlerName() {
        return "onClick";
    }
}
