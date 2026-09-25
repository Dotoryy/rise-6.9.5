package com.alan.clients.script.api.wrapper.impl.event;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.ScriptWrapper;

public abstract class ScriptEvent<T extends Event> extends ScriptWrapper<T> {
    public ScriptEvent(T var1) {
        super(var1);
    }

    public abstract String getHandlerName();
}
