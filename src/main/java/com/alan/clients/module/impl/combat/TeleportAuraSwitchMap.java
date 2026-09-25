package com.alan.clients.module.impl.combat;

import net.minecraft.item.Item.ToolMaterial;

public class TeleportAuraSwitchMap {
    public static final int[] $SwitchMap$net$minecraft$item$Item$ToolMaterial = new int[ToolMaterial.values().length];

    static {
        try {
            $SwitchMap$net$minecraft$item$Item$ToolMaterial[ToolMaterial.WOOD.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }

        try {
            $SwitchMap$net$minecraft$item$Item$ToolMaterial[ToolMaterial.STONE.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }

        try {
            $SwitchMap$net$minecraft$item$Item$ToolMaterial[ToolMaterial.IRON.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }

        try {
            $SwitchMap$net$minecraft$item$Item$ToolMaterial[ToolMaterial.EMERALD.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            $SwitchMap$net$minecraft$item$Item$ToolMaterial[ToolMaterial.GOLD.ordinal()] = 5;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
