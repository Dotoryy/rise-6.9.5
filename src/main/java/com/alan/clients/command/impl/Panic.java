package com.alan.clients.command.impl;

import com.alan.clients.Client;
import com.alan.clients.command.Command;

public final class Panic extends Command {
    public Panic() {
        super("command.panic.description", "panic", "p", "myau");
    }

    @Override
    public void execute(String[] var1) {
        Client.a.g().getAll().stream().filter(var0 -> !var0.getModuleInfo().autoEnabled()).forEach(var0 -> var0.setEnabled(false));
    }
}
