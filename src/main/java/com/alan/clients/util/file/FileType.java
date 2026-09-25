package com.alan.clients.util.file;

public enum FileType {
    ACCOUNT,
    CONFIG,
    INSULT,
    SCRIPT;

    private static final FileType[] $VALUES = ti();

    FileType() {
    }

    private static FileType[] ti() {
        return new FileType[]{ACCOUNT, CONFIG, INSULT, SCRIPT};
    }
}
