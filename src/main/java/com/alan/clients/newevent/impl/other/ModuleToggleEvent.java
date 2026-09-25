package com.alan.clients.newevent.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptModuleToggleEvent;
import lombok.Generated;

public final class ModuleToggleEvent implements Event {
    private Module module;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptModuleToggleEvent(this);
    }

    @Generated
    public Module getModule() {
        return this.module;
    }

    @Generated
    public ModuleToggleEvent(Module module) {
        this.module = module;
    }
}
