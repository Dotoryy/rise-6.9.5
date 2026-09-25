package com.alan.clients.util.player;

import com.alan.clients.util.player.InventoryClickType;
import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class InventoryUtil {
    public static final int aOc = 5;
    public static final int aOd = 9;
    public static final int aOe = 45;
    public static final int aOf = 36;

    private InventoryUtil() {
    }

    public static boolean a(Container container) {
        return IntStream.range(9, 45).allMatch(var1 -> container.getSlot(var1).getHasStack());
    }

    public static void a(Minecraft mc, int var1, int var2, int var3, InventoryClickType var4) {
        mc.playerController.windowClick(var1, var2, var3, var4.ordinal(), mc.thePlayer);
    }
}
