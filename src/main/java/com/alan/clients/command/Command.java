package com.alan.clients.command;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.chat.ChatUtil;
import lombok.Generated;

public abstract class Command implements InstanceAccess {
    private final String description;
    private final String[] expressions;

    public Command(String description, String... expressions) {
        this.description = description;
        this.expressions = expressions;
    }

    public abstract void execute(String[] var1);

    protected final void error() {
        ChatUtil.b("§cInvalid command arguments.");
    }

    protected final void error(String var1) {
        this.error();
        ChatUtil.b("Correct Usage: " + var1);
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String[] getExpressions() {
        return this.expressions;
    }
}
