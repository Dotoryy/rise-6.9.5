package com.alan.clients.module.impl.movement;

import net.minecraft.util.EnumFacing;

public class AutoMLGSwitchMap {
    public static final int[] FACING_SWITCH_MAP = new int[EnumFacing.values().length];

    static {
        try {
            FACING_SWITCH_MAP[EnumFacing.DOWN.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror5) {
        }

        try {
            FACING_SWITCH_MAP[EnumFacing.UP.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }

        try {
            FACING_SWITCH_MAP[EnumFacing.NORTH.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            FACING_SWITCH_MAP[EnumFacing.SOUTH.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            FACING_SWITCH_MAP[EnumFacing.WEST.ordinal()] = 5;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            FACING_SWITCH_MAP[EnumFacing.EAST.ordinal()] = 6;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
