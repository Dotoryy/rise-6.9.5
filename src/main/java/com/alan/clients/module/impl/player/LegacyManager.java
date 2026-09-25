package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.player.InventoryUtil;
import com.alan.clients.util.player.InventoryClickType;
import com.alan.clients.util.player.ItemUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

@ModuleInfo(aliases={"module.player.manager.name", "Manager"}, description="module.player.manager.description", category=Category.PLAYER)
public final class LegacyManager
extends Module {
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        Container container = LegacyManager.aEg.thePlayer.inventoryContainer;
        if (LegacyManager.aEg.currentScreen instanceof GuiInventory && LegacyManager.aEg.thePlayer.openContainer == container) {
            int n2 = -1;
            int n3 = -1;
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int[] nArray = new int[3];
            int[] nArray2 = new int[4];
            Arrays.fill(nArray2, -1);
            Arrays.fill(nArray, -1);
            for (int i2 = 5; i2 < 45; ++i2) {
                ItemStack itemStack = container.getSlot(i2).getStack();
                if (itemStack == null || itemStack.getItem() == null) continue;
                if (itemStack.getItem() instanceof ItemSword) {
                    if (ItemUtil.b(itemStack, container)) {
                        n2 = i2;
                    }
                } else if (itemStack.getItem() instanceof ItemBow) {
                    if (ItemUtil.c(itemStack, container)) {
                        n3 = i2;
                    }
                } else {
                    Item item = itemStack.getItem();
                    if (item instanceof ItemTool) {
                        ItemTool itemTool = (ItemTool)item;
                        int n4 = ItemUtil.d((Item)itemTool);
                        if (ItemUtil.b(itemStack, container, n4)) {
                            nArray[n4] = i2;
                        }
                    } else {
                        Item item2 = itemStack.getItem();
                        if (item2 instanceof ItemArmor) {
                            ItemArmor itemArmor = (ItemArmor)item2;
                            if (ItemUtil.a(itemStack, container, itemArmor.armorType)) {
                                nArray2[itemArmor.armorType] = i2;
                            }
                        }
                    }
                }
                if (ItemUtil.a(itemStack, container)) continue;
                arrayList.add(i2);
            }
            boolean bl = true;
            for (int i3 = 0; i3 < nArray2.length; ++i3) {
                int n5 = nArray2[i3];
                int n6 = i3 + 5;
                if (n5 == -1 || n5 == n6 || container.getSlot(n6).getStack() != null || !bl) continue;
                InventoryUtil.a(aEg, container.windowId, n5, 0, InventoryClickType.QUICK_MOVE);
                return;
            }
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                int n7 = (Integer)iterator.next();
                if (!bl) continue;
                InventoryUtil.a(aEg, container.windowId, n7, 1, InventoryClickType.THROW);
                return;
            }
            int n8 = 36;
            if (n2 != -1) {
                if (n2 != n8 && bl) {
                    InventoryUtil.a(aEg, container.windowId, n2, n8 - 36, InventoryClickType.SWAP);
                    return;
                }
                ++n8;
            }
            if (n3 != -1) {
                if (n3 != n8 && bl) {
                    InventoryUtil.a(aEg, container.windowId, n3, n8 - 36, InventoryClickType.SWAP);
                    return;
                }
                ++n8;
            }
            for (int n9 : nArray) {
                if (n9 == -1) continue;
                if (n9 != n8 && bl) {
                    InventoryUtil.a(aEg, container.windowId, n9, n8 - 36, InventoryClickType.SWAP);
                    return;
                }
                ++n8;
            }
        }
    };
}
