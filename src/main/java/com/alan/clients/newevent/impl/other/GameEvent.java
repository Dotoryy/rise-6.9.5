package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptGameEvent;
import lombok.Generated;

public final class GameEvent extends CancellableEvent {
    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptGameEvent(this);
    }

    @Generated
    public GameEvent() {
    }
}
