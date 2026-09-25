package com.alan.clients.module.impl.combat;

import net.minecraft.item.Item.ToolMaterial;

public class KillAuraSwitchMap {
    public static final int[] TOOL_MATERIAL_SWITCH_MAP = new int[ToolMaterial.values().length];

    static {
        try {
            TOOL_MATERIAL_SWITCH_MAP[ToolMaterial.WOOD.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            TOOL_MATERIAL_SWITCH_MAP[ToolMaterial.STONE.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            TOOL_MATERIAL_SWITCH_MAP[ToolMaterial.IRON.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            TOOL_MATERIAL_SWITCH_MAP[ToolMaterial.GOLD.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
