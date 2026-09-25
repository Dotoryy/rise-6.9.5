package com.alan.clients.util.player;

public enum InventoryClickType {
    PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL;

    private static final InventoryClickType[] $VALUES = vc();

    InventoryClickType() {
    }

    private static InventoryClickType[] vc() {
        return new InventoryClickType[]{PICKUP, QUICK_MOVE, SWAP, CLONE, THROW, QUICK_CRAFT, PICKUP_ALL};
    }
}
