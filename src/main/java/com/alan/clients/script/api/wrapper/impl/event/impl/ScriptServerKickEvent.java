package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.ServerKickEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptServerKickEvent extends ScriptEvent<ServerKickEvent> {
    public ScriptServerKickEvent(ServerKickEvent event) {
        super(event);
    }

    public String[] getReason() {
        return this.wrapped.getMessage().toArray(new String[0]);
    }

    @Override
    public String getHandlerName() {
        return "onServerKick";
    }
}
