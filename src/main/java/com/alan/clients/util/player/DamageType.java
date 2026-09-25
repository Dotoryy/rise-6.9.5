package com.alan.clients.util.player;

public enum DamageType {
    POSITION_ROTATION,
    POSITION;

    private static final DamageType[] $VALUES = uZ();

    DamageType() {
    }

    private static DamageType[] uZ() {
        return new DamageType[]{POSITION_ROTATION, POSITION};
    }
}
