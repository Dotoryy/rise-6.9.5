package com.alan.clients.newevent.impl.motion;

import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptPostMotionEvent;

public final class PostMotionEvent implements Event {
    public PostMotionEvent() {
    }

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptPostMotionEvent(this);
    }
}
