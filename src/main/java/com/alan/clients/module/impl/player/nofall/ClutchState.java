package com.alan.clients.module.impl.player.nofall;

public enum ClutchState {
    IDLE,
    PREDICT,
    ROTATE,
    PLACE,
    WAIT_LAND,
    PICKUP;

    private static final ClutchState[] $VALUES = createValues();

    ClutchState() {
    }

    private static ClutchState[] createValues() {
        return new ClutchState[]{IDLE, PREDICT, ROTATE, PLACE, WAIT_LAND, PICKUP};
    }
}
