package com.alan.clients.component.impl.player.rotationcomponent;

import lombok.Generated;

public enum MovementFix {
    OFF("Off"),
    NORMAL("Rise"),
    TRADITIONAL("Traditional"),
    BACKWARDS_SPRINT("Backwards Sprint");

    final String name;
    private static final MovementFix[] $VALUES = cd();

    @Override
    public String toString() {
        return this.name;
    }

    @Generated
    MovementFix(String name) {
        this.name = name;
    }

    private static MovementFix[] cd() {
        return new MovementFix[]{OFF, NORMAL, TRADITIONAL, BACKWARDS_SPRINT};
    }
}
