package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptTickEvent extends ScriptEvent<TickEvent> {
    public ScriptTickEvent(TickEvent event) {
        super(event);
    }

    @Override
    public String getHandlerName() {
        return "onTick";
    }
}
