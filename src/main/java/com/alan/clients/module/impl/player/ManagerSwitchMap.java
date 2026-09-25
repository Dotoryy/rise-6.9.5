package com.alan.clients.module.impl.player;

import net.minecraft.item.ItemArmor.ArmorMaterial;

public class ManagerSwitchMap {
    public static final int[] adE = new int[ArmorMaterial.values().length];

    static {
        try {
            adE[ArmorMaterial.DIAMOND.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }

        try {
            adE[ArmorMaterial.IRON.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            adE[ArmorMaterial.CHAIN.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            adE[ArmorMaterial.GOLD.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            adE[ArmorMaterial.LEATHER.ordinal()] = 5;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
