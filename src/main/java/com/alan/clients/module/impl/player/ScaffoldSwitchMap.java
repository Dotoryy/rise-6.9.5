package com.alan.clients.module.impl.player;

import net.minecraft.util.EnumFacing;

public class ScaffoldSwitchMap {
    public static final int[] ahg = new int[EnumFacing.values().length];

    static {
        try {
            ahg[EnumFacing.NORTH.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror5) {
        }

        try {
            ahg[EnumFacing.EAST.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }

        try {
            ahg[EnumFacing.SOUTH.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            ahg[EnumFacing.WEST.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            ahg[EnumFacing.DOWN.ordinal()] = 5;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            ahg[EnumFacing.UP.ordinal()] = 6;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
