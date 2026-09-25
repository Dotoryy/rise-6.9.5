package com.alan.clients.command;

public enum CommandResult {
    NOT_A_COMMAND,
    EXECUTED,
    UNKNOWN;

    private static final CommandResult[] $VALUES = aR();

    CommandResult() {
    }

    private static CommandResult[] aR() {
        return new CommandResult[]{NOT_A_COMMAND, EXECUTED, UNKNOWN};
    }
}
