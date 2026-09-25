package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptClickEvent;
import lombok.Generated;

public final class ClickEvent extends CancellableEvent {
    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptClickEvent(this);
    }

    @Generated
    public ClickEvent() {
    }
}
