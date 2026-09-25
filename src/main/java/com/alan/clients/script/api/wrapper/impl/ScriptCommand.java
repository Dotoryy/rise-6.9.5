package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.Client;
import com.alan.clients.command.Command;
import com.alan.clients.script.api.wrapper.ScriptHandlerWrapper;

public final class ScriptCommand extends ScriptHandlerWrapper<Command> {
    public ScriptCommand(Command command) {
        super(command);
    }

    public void unregister() {
        Client.a.getCommandManager().aQ().remove(this.wrapped);
    }

    public String getName() {
        return this.wrapped.getExpressions()[0];
    }

    public String getDescription() {
        return this.wrapped.getDescription();
    }
}
