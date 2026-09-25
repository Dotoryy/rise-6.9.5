package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptPostMotionEvent extends ScriptEvent<PostMotionEvent> {
    public ScriptPostMotionEvent(PostMotionEvent event) {
        super(event);
    }

    @Override
    public String getHandlerName() {
        return "onPostMotion";
    }
}
