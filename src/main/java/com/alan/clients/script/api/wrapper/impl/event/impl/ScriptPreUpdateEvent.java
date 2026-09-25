package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptPreUpdateEvent extends ScriptEvent<PreUpdateEvent> {
    public ScriptPreUpdateEvent(PreUpdateEvent event) {
        super(event);
    }

    @Override
    public String getHandlerName() {
        return "onPreUpdate";
    }
}
