package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.ItemUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.SelectorDetectionComponent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import com.alan.clients.module.impl.player.inventory.InventoryStack;
import com.alan.clients.module.impl.player.OldManagerSwitchMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.be;
import net.minecraft.item.bw;
import net.minecraft.item.cn;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.q;
import net.minecraft.util.DamageSource;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = {"module.player.oldmanager.name", "old manager"}, description = "module.player.manager.description", category = Category.PLAYER)
public class OldManager extends Module {
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 100, 150, 0, 500, 50);
    private final BooleanValue legit = new BooleanValue("Legit", this, false);
    private final BooleanValue dropCustomItems = new BooleanValue("Drop Custom Items", this, false);
    private final BooleanValue useCustomItems = new BooleanValue("Use Custom Items", this, false);
    private final BooleanValue prioritizeSplashPotions = new BooleanValue("Prioritize Splash Potions", this, false);
    private final NumberValue blockLimit = new NumberValue("Block Limit", this, 512, 0, 512, 4);
    private final NumberValue arrowLimit = new NumberValue("Arrow Limit", this, 128, 0, 512, 4);
    private final NumberValue bucketLimit = new NumberValue("Bucket Limit", this, 1, 0, 4, 1);
    private final NumberValue snowballEggLimit = new NumberValue("Snowball/Egg Limit", this, 16, 0, 64, 1);
    private final NumberValue enderPearlLimit = new NumberValue("Ender Pearl Limit", this, 16, 0, 64, 1);
    private final NumberValue fireChargeLimit = new NumberValue("Fire Charge Limit", this, 16, 0, 64, 1);
    private final NumberValue tNTLimit = new NumberValue("TNT Limit", this, 16, 0, 64, 1);
    private final NumberValue swordSlot = new NumberValue("Sword Slot", this, 1, 0, 9, 1);
    private final NumberValue secondSwordSlot = new NumberValue("Second Sword Slot", this, 2, 0, 9, 1);
    private final NumberValue pickaxeSlot = new NumberValue("Pickaxe Slot", this, 2, 0, 9, 1);
    private final NumberValue axeSlot = new NumberValue("Axe Slot", this, 3, 0, 9, 1);
    private final NumberValue shovelSlot = new NumberValue("Shovel Slot", this, 4, 0, 9, 1);
    private final BoundsNumberValue blockSlot = new BoundsNumberValue("Block Slot", this, 5, 5, 0, 9, 1);
    private final BoundsNumberValue potionSlot = new BoundsNumberValue("Potion Slot", this, 6, 6, 0, 9, 1);
    private final NumberValue bowSlot = new NumberValue("Bow Slot", this, 7, 0, 9, 1);
    private final NumberValue rodSlot = new NumberValue("Rod Slot", this, 8, 0, 9, 1);
    private final BoundsNumberValue foodSlot = new BoundsNumberValue("Food Slot", this, 9, 9, 0, 9, 1);
    private final int INVENTORY_ROWS = 4;
    private final int INVENTORY_COLUMNS = 9;
    private final int ARMOR_SLOTS = 4;
    private final int INVENTORY_SLOTS = 40;
    private final StopWatch stopwatch = new StopWatch();
    private int chestTicks;
    private int attackTicks;
    private int placeTicks;
    private boolean moved;
    private boolean open;
    private long nextClick;
    private int clicksThisTick;
    private int extraSprintTicks;
    @EventLink(value = 1)
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (aEg.thePlayer.ticksExisted > 40) {
            if (aEg.currentScreen instanceof GuiChest) {
                this.chestTicks = 0;
            } else {
                this.chestTicks++;
            }

            this.attackTicks++;
            this.placeTicks++;
            if (this.legit.wo() && !(aEg.currentScreen instanceof GuiInventory)) {
                this.stopwatch.aX();
            } else if (this.stopwatch.T(this.nextClick) && this.chestTicks >= 10 && this.attackTicks >= 10 && this.placeTicks >= 10 && !this.e(Scaffold.class).isEnabled()) {
                if (this.e(InventoryMove.class).isEnabled() || aEg.currentScreen instanceof GuiInventory) {
                    this.moved = false;
                    int i = -1;
                    int j = -1;
                    int k = -1;
                    int l = -1;
                    int i1 = -1;
                    int j1 = -1;
                    int k1 = -1;
                    int l1 = -1;
                    int i2 = -1;
                    ArrayList arraylist = new ArrayList();
                    ArrayList arraylist1 = new ArrayList();
                    int j2 = -1;
                    int k2 = -1;
                    ArrayList arraylist2 = new ArrayList();
                    int l2 = 0;
                    int i3 = 0;
                    int j3 = 0;
                    int k3 = 0;
                    int l3 = 0;
                    int i4 = 0;
                    int j4 = 0;
                    ArrayList arraylist3 = new ArrayList();
                    ArrayList arraylist4 = new ArrayList();
                    ArrayList arraylist5 = new ArrayList();
                    ArrayList arraylist6 = new ArrayList();
                    ArrayList arraylist7 = new ArrayList();
                    ArrayList arraylist8 = new ArrayList();
                    ArrayList arraylist9 = new ArrayList();
                    boolean flag = this.swordSlot.wo().intValue() != 0 && this.secondSwordSlot.wo().intValue() != 0 && this.secondSwordSlot.wo().intValue() != this.swordSlot.wo().intValue();

                    for (int k4 = 0; k4 < 40; k4++) {
                        ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(k4);
                        if (itemstack != null) {
                            Item item = itemstack.getItem();
                            if (!ItemUtil.useful(itemstack)) {
                                this.throwItem(k4);
                            }

                            if (item == Items.arrow) {
                                i3 += itemstack.stackSize;
                                arraylist4.add(k4);
                            } else if (item == Items.bucket || item == Items.water_bucket) {
                                j3 += itemstack.stackSize;
                                arraylist5.add(k4);
                            } else if (item == Items.snowball || item == Items.egg) {
                                k3 += itemstack.stackSize;
                                arraylist6.add(k4);
                            } else if (item == Items.ender_pearl) {
                                l3 += itemstack.stackSize;
                                arraylist7.add(k4);
                            } else if (item == Items.fire_charge) {
                                i4 += itemstack.stackSize;
                                arraylist8.add(k4);
                            }

                            if (item instanceof ItemBlock) {
                                Block block = ((ItemBlock)item).getBlock();
                                if (block instanceof BlockTNT) {
                                    j4 += itemstack.stackSize;
                                    arraylist9.add(k4);
                                }
                            }

                            if (item instanceof ItemArmor itemarmor) {
                                int l4 = this.armorRating(itemstack);
                                switch (itemarmor.armorType) {
                                    case 0:
                                        if (i == -1 || l4 > this.armorRating(aEg.thePlayer.inventory.getStackInSlot(i))) {
                                            i = k4;
                                        }
                                        break;
                                    case 1:
                                        if (j == -1 || l4 > this.armorRating(aEg.thePlayer.inventory.getStackInSlot(j))) {
                                            j = k4;
                                        }
                                        break;
                                    case 2:
                                        if (k == -1 || l4 > this.armorRating(aEg.thePlayer.inventory.getStackInSlot(k))) {
                                            k = k4;
                                        }
                                        break;
                                    case 3:
                                        if (l == -1 || l4 > this.armorRating(aEg.thePlayer.inventory.getStackInSlot(l))) {
                                            l = k4;
                                        }
                                }
                            }

                            if (item instanceof ItemSword && this.swordSlot.wo().intValue() != 0) {
                                if (i1 == -1) {
                                    i1 = k4;
                                } else if (this.damage(itemstack) > this.damage(aEg.thePlayer.inventory.getStackInSlot(i1))) {
                                    if (flag) {
                                        j1 = i1;
                                    }

                                    i1 = k4;
                                } else if (flag && (j1 == -1 || this.damage(itemstack) > this.damage(aEg.thePlayer.inventory.getStackInSlot(j1)))) {
                                    j1 = k4;
                                }
                            }

                            if (item instanceof bw) {
                                if (k1 == -1) {
                                    k1 = k4;
                                } else if (this.mineSpeed(itemstack) > this.mineSpeed(aEg.thePlayer.inventory.getStackInSlot(k1))) {
                                    k1 = k4;
                                }

                                if (k4 != k1) {
                                    this.throwItem(k4);
                                }
                            }

                            if (item instanceof ItemAxe) {
                                if (l1 == -1) {
                                    l1 = k4;
                                } else if (this.mineSpeed(itemstack) > this.mineSpeed(aEg.thePlayer.inventory.getStackInSlot(l1))) {
                                    l1 = k4;
                                }

                                if (k4 != l1) {
                                    this.throwItem(k4);
                                }
                            }

                            if (item instanceof cn) {
                                if (i2 == -1) {
                                    i2 = k4;
                                } else if (this.mineSpeed(itemstack) > this.mineSpeed(aEg.thePlayer.inventory.getStackInSlot(i2))) {
                                    i2 = k4;
                                }

                                if (k4 != i2) {
                                    this.throwItem(k4);
                                }
                            }

                            if (item instanceof ItemBlock) {
                                l2 += itemstack.stackSize;
                                arraylist3.add(k4);
                                arraylist.add(new InventoryStack(itemstack, k4));
                            }

                            if (item instanceof ItemPotion) {
                                arraylist1.add(new InventoryStack(itemstack, k4));
                            }

                            if (item instanceof ItemBow) {
                                if (j2 == -1) {
                                    j2 = k4;
                                } else if (this.bowRating(itemstack) > this.bowRating(aEg.thePlayer.inventory.getStackInSlot(j2))) {
                                    j2 = k4;
                                }

                                if (k4 != j2) {
                                    this.throwItem(k4);
                                }
                            }

                            if (item instanceof ItemFood && this.foodSlot.wo().intValue() != 0) {
                                arraylist2.add(new InventoryStack(itemstack, k4));
                            }

                            if (item instanceof be) {
                                if (k2 == -1) {
                                    k2 = k4;
                                }

                                if (k4 != k2) {
                                    this.throwItem(k4);
                                }
                            }
                        }
                    }

                    for (int i5 = 0; i5 < 40; i5++) {
                        ItemStack itemstack1 = aEg.thePlayer.inventory.getStackInSlot(i5);
                        if (itemstack1 != null) {
                            Item item1 = itemstack1.getItem();
                            if (item1 instanceof ItemArmor itemarmor1) {
                                switch (itemarmor1.armorType) {
                                    case 0:
                                        if (i5 != i) {
                                            this.throwItem(i5);
                                        }
                                        break;
                                    case 1:
                                        if (i5 != j) {
                                            this.throwItem(i5);
                                        }
                                        break;
                                    case 2:
                                        if (i5 != k) {
                                            this.throwItem(i5);
                                        }
                                        break;
                                    case 3:
                                        if (i5 != l) {
                                            this.throwItem(i5);
                                        }
                                }
                            }

                            if (this.swordSlot.wo().intValue() != 0 && item1 instanceof ItemSword && i5 != i1 && (!flag || i5 != j1)) {
                                this.throwItem(i5);
                            }
                        }
                    }

                    if (i != -1 && i != 39) {
                        this.equipItem(i);
                    }

                    if (j != -1 && j != 38) {
                        this.equipItem(j);
                    }

                    if (k != -1 && k != 37) {
                        this.equipItem(k);
                    }

                    if (l != -1 && l != 36) {
                        this.equipItem(l);
                    }

                    if (this.blockSlot.wo().intValue() != 0 && this.blockSlot.wA().intValue() != 0) {
                        int j5 = Math.min(this.blockSlot.wo().intValue(), this.blockSlot.wA().intValue());
                        int k5 = Math.max(this.blockSlot.wo().intValue(), this.blockSlot.wA().intValue());
                        int l5 = k5 - j5 + 1;
                        arraylist.sort(Comparator.comparingInt(InventoryStack::jI).reversed());

                        for (int i6 = 0; i6 < l5 && arraylist.size() > i6; i6++) {
                            int i13 = j5 - 1 + i6;
                            int j13 = ((InventoryStack)arraylist.get(i6)).jH();
                            if (!this.isAlreadyInSlot(j13, i13)) {
                                this.h(j13, i13);
                            }
                        }
                    }

                    if (l2 > this.blockLimit.wo().intValue()) {
                        int j6 = l2 - this.blockLimit.wo().intValue();

                        for (int k6 : (Iterable<Integer>)arraylist3.reversed()) {
                            if (j6 <= 0) {
                                break;
                            }

                            ItemStack itemstack2 = aEg.thePlayer.inventory.getStackInSlot(k6);
                            int k13 = itemstack2.stackSize;
                            if (j6 >= k13) {
                                this.throwItem(k6);
                                j6 -= k13;
                            } else {
                                this.dropPartialStack(k6, k13 - j6);
                                j6 = 0;
                            }
                        }
                    }

                    if (i3 > this.arrowLimit.wo().intValue()) {
                        int l6 = i3 - this.arrowLimit.wo().intValue();

                        for (int i7 : (Iterable<Integer>)arraylist4) {
                            if (l6 <= 0) {
                                break;
                            }

                            ItemStack itemstack3 = aEg.thePlayer.inventory.getStackInSlot(i7);
                            int l13 = itemstack3.stackSize;
                            if (l6 >= l13) {
                                this.throwItem(i7);
                                l6 -= l13;
                            } else {
                                this.dropPartialStack(i7, l13 - l6);
                                l6 = 0;
                            }
                        }
                    }

                    if (j3 > this.bucketLimit.wo().intValue()) {
                        int j7 = j3 - this.bucketLimit.wo().intValue();

                        for (int k7 : (Iterable<Integer>)arraylist5) {
                            if (j7 <= 0) {
                                break;
                            }

                            ItemStack itemstack4 = aEg.thePlayer.inventory.getStackInSlot(k7);
                            int i14 = itemstack4.stackSize;
                            if (j7 >= i14) {
                                this.throwItem(k7);
                                j7 -= i14;
                            } else {
                                this.dropPartialStack(k7, i14 - j7);
                                j7 = 0;
                            }
                        }
                    }

                    if (k3 > this.snowballEggLimit.wo().intValue()) {
                        int l7 = k3 - this.snowballEggLimit.wo().intValue();

                        for (int i8 : (Iterable<Integer>)arraylist6) {
                            if (l7 <= 0) {
                                break;
                            }

                            ItemStack itemstack5 = aEg.thePlayer.inventory.getStackInSlot(i8);
                            int j14 = itemstack5.stackSize;
                            if (l7 >= j14) {
                                this.throwItem(i8);
                                l7 -= j14;
                            } else {
                                this.dropPartialStack(i8, j14 - l7);
                                l7 = 0;
                            }
                        }
                    }

                    if (l3 > this.enderPearlLimit.wo().intValue()) {
                        int j8 = l3 - this.enderPearlLimit.wo().intValue();

                        for (int k8 : (Iterable<Integer>)arraylist7) {
                            if (j8 <= 0) {
                                break;
                            }

                            ItemStack itemstack6 = aEg.thePlayer.inventory.getStackInSlot(k8);
                            int k14 = itemstack6.stackSize;
                            if (j8 >= k14) {
                                this.throwItem(k8);
                                j8 -= k14;
                            } else {
                                this.dropPartialStack(k8, k14 - j8);
                                j8 = 0;
                            }
                        }
                    }

                    if (i4 > this.fireChargeLimit.wo().intValue()) {
                        int l8 = i4 - this.fireChargeLimit.wo().intValue();

                        for (int i9 : (Iterable<Integer>)arraylist8) {
                            if (l8 <= 0) {
                                break;
                            }

                            ItemStack itemstack7 = aEg.thePlayer.inventory.getStackInSlot(i9);
                            int l14 = itemstack7.stackSize;
                            if (l8 >= l14) {
                                this.throwItem(i9);
                                l8 -= l14;
                            } else {
                                this.dropPartialStack(i9, l14 - l8);
                                l8 = 0;
                            }
                        }
                    }

                    if (j4 > this.tNTLimit.wo().intValue()) {
                        int j9 = j4 - this.tNTLimit.wo().intValue();

                        for (int k9 : (Iterable<Integer>)arraylist9) {
                            if (j9 <= 0) {
                                break;
                            }

                            ItemStack itemstack8 = aEg.thePlayer.inventory.getStackInSlot(k9);
                            int i15 = itemstack8.stackSize;
                            if (j9 >= i15) {
                                this.throwItem(k9);
                                j9 -= i15;
                            } else {
                                this.dropPartialStack(k9, i15 - j9);
                                j9 = 0;
                            }
                        }
                    }

                    int l9 = this.swordSlot.wo().intValue() - 1;
                    if (this.swordSlot.wo().intValue() != 0 && i1 != -1 && !this.isAlreadyInSlot(i1, l9)) {
                        this.h(i1, l9);
                    }

                    int i10 = this.secondSwordSlot.wo().intValue() - 1;
                    if (flag && j1 != -1 && !this.isAlreadyInSlot(j1, i10)) {
                        this.h(j1, i10);
                    }

                    int j10 = this.pickaxeSlot.wo().intValue() - 1;
                    if (!this.conflictsWithSwordSlot(this.pickaxeSlot, i1, j1) && this.pickaxeSlot.wo().intValue() != 0 && k1 != -1 && !this.isAlreadyInSlot(k1, j10)) {
                        this.h(k1, j10);
                    }

                    int k10 = this.axeSlot.wo().intValue() - 1;
                    if (!this.conflictsWithSwordSlot(this.axeSlot, i1, j1) && this.axeSlot.wo().intValue() != 0 && l1 != -1 && !this.isAlreadyInSlot(l1, k10)) {
                        this.h(l1, k10);
                    }

                    int l10 = this.shovelSlot.wo().intValue() - 1;
                    if (!this.conflictsWithSwordSlot(this.shovelSlot, i1, j1) && this.shovelSlot.wo().intValue() != 0 && i2 != -1 && !this.isAlreadyInSlot(i2, l10)) {
                        this.h(i2, l10);
                    }

                    int i11 = this.bowSlot.wo().intValue() - 1;
                    if (!this.conflictsWithSwordSlot(this.bowSlot, i1, j1) && this.bowSlot.wo().intValue() != 0 && j2 != -1 && !this.isAlreadyInSlot(j2, i11)) {
                        this.h(j2, i11);
                    }

                    int j11 = this.rodSlot.wo().intValue() - 1;
                    boolean flag1 = j2 != -1 && this.rodSlot.wo().intValue() == this.bowSlot.wo().intValue();
                    if (!this.conflictsWithSwordSlot(this.rodSlot, i1, j1) && this.rodSlot.wo().intValue() != 0 && k2 != -1 && !this.isAlreadyInSlot(k2, j11) && !flag1) {
                        this.h(k2, j11);
                    }

                    if (this.potionSlot.wo().intValue() != 0 && this.potionSlot.wA().intValue() != 0) {
                        int k11 = Math.min(this.potionSlot.wo().intValue(), this.potionSlot.wA().intValue());
                        int l11 = Math.max(this.potionSlot.wo().intValue(), this.potionSlot.wA().intValue()) - k11 + 1;
                        arraylist1.sort(
                            (var1x, var2) -> {
                                ItemPotion itempotion = (ItemPotion)((InventoryStack)var1x).bO().getItem();
                                ItemPotion itempotion1 = (ItemPotion)((InventoryStack)var2).bO().getItem();
                                if (this.prioritizeSplashPotions.wo()) {
                                    boolean flag3 = ItemPotion.isSplash(((InventoryStack)var1x).bO().getMetadata());
                                    boolean flag4 = ItemPotion.isSplash(((InventoryStack)var2).bO().getMetadata());
                                    if (flag3 && !flag4) {
                                        return -1;
                                    }

                                    if (!flag3 && flag4) {
                                        return 1;
                                    }
                                }

                                List list = itempotion.getEffects(((InventoryStack)var1x).bO());
                                List list1 = itempotion1.getEffects(((InventoryStack)var2).bO());
                                if (list.isEmpty() && list1.isEmpty()) {
                                    return 0;
                                } else if (list.isEmpty()) {
                                    return 1;
                                }
                                return list1.isEmpty()
                                    ? -1
                                    : Integer.compare(
                                        PlayerUtil.potionRanking(itempotion1.getEffects(((InventoryStack)var2).bO()).get(0).getPotionID()),
                                        PlayerUtil.potionRanking(itempotion.getEffects(((InventoryStack)var1x).bO()).get(0).getPotionID())
                                    );
                            }
                        );

                        for (int i12 = 0; i12 < l11 && arraylist1.size() > i12; i12++) {
                            int j15 = k11 - 1 + i12;
                            int k15 = ((InventoryStack)arraylist1.get(i12)).jH();
                            if (!this.isAlreadyInSlot(k15, j15)) {
                                this.h(k15, j15);
                            }
                        }
                    }

                    if (this.foodSlot.wo().intValue() != 0 && this.foodSlot.wA().intValue() != 0) {
                        int j12 = Math.min(this.foodSlot.wo().intValue(), this.foodSlot.wA().intValue());
                        int k12 = Math.max(this.foodSlot.wo().intValue(), this.foodSlot.wA().intValue()) - j12 + 1;
                        arraylist2.sort((var0, var1x) -> {
                            ItemFood itemfood = (ItemFood)((InventoryStack)var0).bO().getItem();
                            return Float.compare(((ItemFood)((InventoryStack)var1x).bO().getItem()).getSaturationModifier(((InventoryStack)var1x).bO()), itemfood.getSaturationModifier(((InventoryStack)var0).bO()));
                        });

                        for (int l12 = 0; l12 < k12 && arraylist2.size() > l12; l12++) {
                            int l15 = j12 - 1 + l12;
                            int i16 = ((InventoryStack)arraylist2.get(l12)).jH();
                            if (!this.isAlreadyInSlot(i16, l15)) {
                                this.h(i16, l15);
                            }
                        }
                    }

                    if (this.canOpenInventory() && !this.moved) {
                        this.closeInventory();
                    }
                }
            } else {
                this.closeInventory();
            }
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> this.attackTicks = 0;
    @EventLink
    public final Listener<SprintEvent> onSprint = var1 -> {
        if (this.isGrimBypass() && this.clicksThisTick > 0) {
            this.extraSprintTicks = Math.max(this.extraSprintTicks, this.getExtraSprintTicks());
        }

        this.clicksThisTick = 0;
        if (this.isGrimBypass() && this.extraSprintTicks > 0) {
            aEg.thePlayer.setSprinting(false);
            this.extraSprintTicks--;
        } else if (!this.isGrimBypass()) {
            this.extraSprintTicks = 0;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        if (var1.dq() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement c08packetplayerblockplacement = (C08PacketPlayerBlockPlacement)var1.dq();
            if (c08packetplayerblockplacement.getStack() == null || c08packetplayerblockplacement.getStack().getItem() != Items.water_bucket) {
                this.placeTicks = 0;
            }
        }
    };

    public OldManager() {
    }

    @Override
    public void onDisable() {
        if (this.canOpenInventory()) {
            this.closeInventory();
        }

        this.clicksThisTick = 0;
        this.extraSprintTicks = 0;
    }

    private void openInventory() {
        if (!this.open) {
            PacketUtil.send(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            this.open = true;
            this.countClick();
        }
    }

    private void closeInventory() {
        if (this.open) {
            PacketUtil.send(new q(aEg.thePlayer.inventoryContainer.windowId));
            this.open = false;
            this.countClick();
        }
    }

    private boolean canOpenInventory() {
        boolean flag = this.e(InventoryMove.class).isEnabled();
        boolean flag1 = !(aEg.currentScreen instanceof GuiInventory);
        boolean flag2 = this.e(Scaffold.class).isEnabled();
        return flag && flag1 && !flag2;
    }

    private void throwItem(int var1) {
        if ((!this.moved || this.nextClick <= 0L) && !SelectorDetectionComponent.a(var1, !this.dropCustomItems.wo())) {
            if (this.canOpenInventory()) {
                this.openInventory();
            }

            aEg.playerController.windowClick(aEg.thePlayer.inventoryContainer.windowId, this.slot(var1), 1, 4, aEg.thePlayer);
            this.countClick();
            this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
            this.stopwatch.aX();
            this.moved = true;
        }
    }

    private void dropPartialStack(int var1, int var2) {
        if ((!this.moved || this.nextClick <= 0L) && !SelectorDetectionComponent.a(var1, !this.dropCustomItems.wo())) {
            if (this.canOpenInventory()) {
                this.openInventory();
            }

            aEg.playerController.windowClick(aEg.thePlayer.inventoryContainer.windowId, this.slot(var1), 0, 0, aEg.thePlayer);
            System.out.println(var2);

            for (int i = 0; i < var2; i++) {
                aEg.playerController.windowClick(aEg.thePlayer.inventoryContainer.windowId, this.slot(var1), 1, 0, aEg.thePlayer);
            }

            this.countClick();
            this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
            this.stopwatch.aX();
            this.moved = true;
        }
    }

    private void h(int var1, int var2) {
        if ((!this.moved || this.nextClick <= 0L) && !SelectorDetectionComponent.a(var1, !this.useCustomItems.wo())) {
            if (this.canOpenInventory()) {
                this.openInventory();
            }

            if (var2 < 0 || var2 > 8) {
                return;
            }

            aEg.playerController.windowClick(aEg.thePlayer.inventoryContainer.windowId, this.slot(var1), var2, 2, aEg.thePlayer);
            this.countClick();
            this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
            this.stopwatch.aX();
            this.moved = true;
        }
    }

    private void equipItem(int var1) {
        if ((!this.moved || this.nextClick <= 0L) && !SelectorDetectionComponent.a(var1, !this.useCustomItems.wo())) {
            if (this.canOpenInventory()) {
                this.openInventory();
            }

            aEg.playerController.windowClick(aEg.thePlayer.inventoryContainer.windowId, this.slot(var1), 0, 1, aEg.thePlayer);
            this.countClick();
            this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
            this.stopwatch.aX();
            this.moved = true;
        }
    }

    private void countClick() {
        if (this.isGrimBypass()) {
            this.clicksThisTick++;
        }
    }

    private boolean isGrimBypass() {
        InventoryMove inventorymove = this.e(InventoryMove.class);
        return inventorymove != null && inventorymove.isEnabled() && inventorymove.isGrimBypass();
    }

    private int getExtraSprintTicks() {
        InventoryMove inventorymove = this.e(InventoryMove.class);
        return inventorymove == null ? 9 : inventorymove.getExtraSprintTicks();
    }

    public boolean isSorting() {
        return this.stopwatch.T(this.nextClick) && this.chestTicks >= 10 && this.attackTicks >= 10 && this.placeTicks >= 10 && (this.canOpenInventory() || aEg.currentScreen instanceof GuiInventory);
    }

    private boolean conflictsWithSwordSlot(NumberValue numberValue, int var2, int var3) {
        int i = numberValue.wo().intValue();
        return i != 0 && (var2 != -1 && this.swordSlot.wo().intValue() == i || var3 != -1 && this.secondSwordSlot.wo().intValue() == i);
    }

    private float damage(ItemStack stack) {
        ItemSword itemsword = (ItemSword)stack.getItem();
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
        return (float)(itemsword.getDamageVsEntity() + i * 1.25 + j * 2.5);
    }

    private float bowRating(ItemStack stack) {
        ItemBow itembow = (ItemBow)stack.getItem();
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
    }

    private float mineSpeed(ItemStack stack) {
        Item item = stack.getItem();
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);

        short short1 = switch (i) {
            case 1 -> 30;
            case 2 -> 69;
            case 3 -> 120;
            case 4 -> 186;
            case 5 -> 271;
            default -> 0;
        };
        if (item instanceof bw) {
            return ((bw)item).getToolMaterial().getEfficiencyOnProperMaterial() + short1;
        } else if (item instanceof cn) {
            return ((cn)item).getToolMaterial().getEfficiencyOnProperMaterial() + short1;
        }
        return item instanceof ItemAxe ? ((ItemAxe)item).getToolMaterial().getEfficiencyOnProperMaterial() + short1 : 0.0F;
    }

    private int armorRating(ItemStack stack) {
        ItemArmor itemarmor = (ItemArmor)stack.getItem();
        int i = itemarmor.damageReduceAmount;

        byte b0 = switch (itemarmor.getArmorMaterial()) {
            case DIAMOND -> 5;
            case IRON -> 4;
            case CHAIN -> 3;
            case GOLD -> 2;
            case LEATHER -> 1;
            default -> 0;
        };
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
        int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
        int i1 = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
        return i * 100 + b0 * 10 + j * 6 + (k + l + i1) * 2;
    }

    private int armorReduction(ItemStack stack) {
        return ((ItemArmor)stack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{stack}, DamageSource.generic);
    }

    private boolean isAlreadyInSlot(int var1, int var2) {
        return var2 >= 0 && var2 <= 8 && var1 >= 0 && var1 <= 8 && var1 == var2;
    }

    private int slot(int var1) {
        if (var1 >= 0 && var1 <= 8) {
            return var1 + 36;
        } else if (var1 >= 9 && var1 <= 35) {
            return var1;
        } else if (var1 == 36) {
            return 8;
        } else if (var1 == 37) {
            return 7;
        } else if (var1 == 38) {
            return 6;
        }
        return var1 == 39 ? 5 : var1;
    }

    @Generated
    public boolean isMoved() {
        return this.moved;
    }

    @Generated
    public boolean isOpen() {
        return this.open;
    }
}
