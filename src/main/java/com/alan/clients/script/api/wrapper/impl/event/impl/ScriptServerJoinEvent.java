package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptServerJoinEvent extends ScriptEvent<ServerJoinEvent> {
    public ScriptServerJoinEvent(ServerJoinEvent event) {
        super(event);
    }

    public String getIp() {
        return this.wrapped.getIp();
    }

    public int getPort() {
        return this.wrapped.getPort();
    }

    @Override
    public String getHandlerName() {
        return "onServerJoin";
    }
}
