package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptKeyboardInputEvent extends ScriptEvent<KeyboardInputEvent> {
    public ScriptKeyboardInputEvent(KeyboardInputEvent event) {
        super(event);
    }

    public int getKey() {
        return this.wrapped.getKeyCode();
    }

    @Override
    public String getHandlerName() {
        return "onKeyboardInput";
    }
}
