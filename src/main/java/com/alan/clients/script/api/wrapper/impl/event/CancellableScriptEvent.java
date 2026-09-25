package com.alan.clients.script.api.wrapper.impl.event;

import com.alan.clients.newevent.CancellableEvent;

public abstract class CancellableScriptEvent<T extends CancellableEvent> extends ScriptEvent<T> {
    public CancellableScriptEvent(T var1) {
        super(var1);
    }

    public boolean isCancelled() {
        return this.wrapped.isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        this.wrapped.setCancelled(cancelled);
    }
}
