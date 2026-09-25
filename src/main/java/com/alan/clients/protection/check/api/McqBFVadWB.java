package com.alan.clients.protection.check.api;

public enum McqBFVadWB {
    INITIALIZE,
    REPETITIVE,
    POST_INITIALIZE,
    JOIN;

    private static final McqBFVadWB[] $VALUES = oa();

    McqBFVadWB() {
    }

    private static McqBFVadWB[] oa() {
        return new McqBFVadWB[]{INITIALIZE, REPETITIVE, POST_INITIALIZE, JOIN};
    }
}
