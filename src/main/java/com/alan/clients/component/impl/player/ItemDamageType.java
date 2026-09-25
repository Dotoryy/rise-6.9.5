package com.alan.clients.component.impl.player;

public enum ItemDamageType {
    BOW,
    ROD,
    CLAY,
    PROJECTILES;

    private static final ItemDamageType[] $VALUES = bb();

    ItemDamageType() {
    }

    private static ItemDamageType[] bb() {
        return new ItemDamageType[]{BOW, ROD, CLAY, PROJECTILES};
    }
}
