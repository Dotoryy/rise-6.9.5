package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.ModuleToggleEvent;
import com.alan.clients.script.api.wrapper.impl.ScriptModule;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptModuleToggleEvent extends ScriptEvent<ModuleToggleEvent> {
    public ScriptModuleToggleEvent(ModuleToggleEvent event) {
        super(event);
    }

    public ScriptModule getModule() {
        return new ScriptModule(this.wrapped.getModule());
    }

    @Override
    public String getHandlerName() {
        return "onModuleToggle";
    }
}
