package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptPreUpdateEvent;

public class PreUpdateEvent implements Event {
    public PreUpdateEvent() {
    }

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptPreUpdateEvent(this);
    }
}
