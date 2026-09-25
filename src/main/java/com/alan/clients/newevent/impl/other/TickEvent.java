package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptTickEvent;

public final class TickEvent extends CancellableEvent {
    public TickEvent() {
    }

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptTickEvent(this);
    }
}
