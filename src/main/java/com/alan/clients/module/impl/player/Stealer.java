package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.ItemUtil;
import com.alan.clients.component.impl.player.GUIDetectionComponent;
import com.alan.clients.component.impl.player.SelectorDetectionComponent;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = {"module.player.stealer.name", "Stealer"}, description = "module.player.stealer.description", category = Category.PLAYER)
public class Stealer extends Module {
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 100, 150, 0, 500, 50);
    private final BoundsNumberValue firstItemDelay = new BoundsNumberValue("First Item Delay", this, 0, 0, 0, 500, 50);
    private final BooleanValue ignoreTrash = new BooleanValue("Ignore Trash", this, true);
    private final BooleanValue respectManagerRules = new BooleanValue("Respect Manager Rules", this, true);
    private final BooleanValue guiDetection = new BooleanValue("Gui Detection", this, true);
    private final StopWatch stopwatch = new StopWatch();
    private long nextClick;
    private int lastClick;
    private int lastSteal;
    private int chestOpenTicks;
    private boolean closedScreen;
    private boolean appliedFirstItemDelay;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.currentScreen instanceof GuiChest) {
            this.chestOpenTicks++;
            this.closedScreen = false;
            ContainerChest containerchest = (ContainerChest)aEg.thePlayer.openContainer;
            if (this.chestOpenTicks == 1 && !this.appliedFirstItemDelay) {
                int i = this.firstItemDelay.wo().intValue();
                int j = this.firstItemDelay.wA().intValue();
                if (i > 0 || j > 0) {
                    this.nextClick = Math.round(MathUtil.l(i, j));
                    this.stopwatch.aX();
                    this.appliedFirstItemDelay = true;
                    return;
                }

                this.appliedFirstItemDelay = true;
            }

            if (this.guiDetection.wo() && GUIDetectionComponent.inGUI() || !this.stopwatch.T(this.nextClick)) {
                return;
            }

            this.lastSteal++;

            for (int k = 0; k < containerchest.inventorySlots.size(); k++) {
                ItemStack itemstack = containerchest.getLowerChestInventory().getStackInSlot(k);
                if (itemstack != null && this.lastSteal > 1 && (!this.ignoreTrash.wo() || ItemUtil.useful(itemstack)) && (!this.respectManagerRules.wo() || !this.shouldIgnore(itemstack))) {
                    this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
                    aEg.playerController.windowClick(containerchest.windowId, k, 0, 1, aEg.thePlayer);
                    this.stopwatch.aX();
                    this.lastClick = 0;
                    if (this.nextClick > 0L) {
                        return;
                    }
                }
            }

            this.lastClick++;
            if (this.lastClick > 1 && this.chestOpenTicks > 2.0 + 2.0 * Math.random()) {
                aEg.thePlayer.closeScreen();
                this.closedScreen = true;
            }
        } else {
            this.lastClick = 0;
            this.chestOpenTicks = 0;
            this.lastSteal = 0;
            this.appliedFirstItemDelay = false;
        }
    };

    public Stealer() {
    }

    public boolean hasClosedScreen() {
        return this.closedScreen;
    }

    private boolean shouldIgnore(ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            Item item = stack.getItem();
            Manager manager = this.e(Manager.class);
            Container container = aEg.thePlayer.inventoryContainer;
            if (manager != null && SelectorDetectionComponent.a(stack, true) && manager.shouldDropCustomItems()) {
                return true;
            } else if (!ItemUtil.useful(stack)) {
                return true;
            } else if (item instanceof ItemSword) {
                return !ItemUtil.b(stack, container);
            } else if (item instanceof ItemTool) {
                return !ItemUtil.b(stack, container, ItemUtil.d((ItemTool)item));
            } else if (item instanceof ItemBow) {
                return !ItemUtil.c(stack, container);
            } else if (item instanceof ItemArmor) {
                return !ItemUtil.a(stack, container, ((ItemArmor)item).armorType);
            } else if (item instanceof net.minecraft.item.be) {
                return this.countItems(var0 -> var0 instanceof net.minecraft.item.be) >= 1;
            }
            int i = manager != null ? manager.getArrowLimit() : 128;
            int j = manager != null ? manager.getBucketLimit() : 1;
            int k = manager != null ? manager.getSnowballEggLimit() : 16;
            int l = manager != null ? manager.getEnderPearlLimit() : 16;
            int i1 = manager != null ? manager.getBlockLimit() : 512;
            if (item == Items.arrow) {
                return this.countItem(Items.arrow) + stack.stackSize > i;
            } else if (item == Items.ender_pearl) {
                return this.countItem(Items.ender_pearl) + stack.stackSize > l;
            } else if (item == Items.bucket || item == Items.water_bucket || item == Items.lava_bucket || item == Items.milk_bucket) {
                return this.countItem(Items.bucket) + this.countItem(Items.water_bucket) + this.countItem(Items.lava_bucket) + this.countItem(Items.milk_bucket) + stack.stackSize > j;
            } else if (item == Items.snowball || item == Items.egg) {
                return this.countItem(Items.snowball) + this.countItem(Items.egg) + stack.stackSize > k;
            }
            return item instanceof ItemBlock ? this.countItems(var0 -> var0 instanceof ItemBlock) + stack.stackSize > i1 : false;
        }
        return true;
    }

    private int countItem(Item item) {
        int i = 0;

        for (int j = 0; j <= 39; j++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(j);
            if (itemstack != null && itemstack.getItem() == item) {
                i += itemstack.stackSize;
            }
        }

        return i;
    }

    private int countItems(Predicate<Item> predicate) {
        int i = 0;

        for (int j = 0; j <= 39; j++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(j);
            if (itemstack != null && predicate.test(itemstack.getItem())) {
                i += itemstack.stackSize;
            }
        }

        return i;
    }

    @Generated
    public int kw() {
        return this.chestOpenTicks;
    }
}
