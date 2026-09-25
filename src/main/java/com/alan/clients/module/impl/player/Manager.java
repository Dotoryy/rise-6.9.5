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
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.ItemUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.player.SelectorDetectionComponent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import com.alan.clients.module.impl.player.ManagerSwitchMap;
import com.alan.clients.util.render.IntGatherer;
import com.alan.clients.module.impl.player.inventory.InventoryScan;
import com.alan.clients.module.impl.player.inventory.SlotStack;
import com.alan.clients.module.impl.player.inventory.ItemScorer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = {"module.player.manager.name", "Manager"}, description = "module.player.manager.description", category = Category.PLAYER)
public class Manager extends Module {
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
    private final NumberValue swordSlot = new NumberValue("Sword Slot", this, 1, 0, 9, 1);
    private final NumberValue secondSwordSlot = new NumberValue("Second Sword Slot", this, 2, 0, 9, 1);
    private final NumberValue pickaxeSlot = new NumberValue("Pickaxe Slot", this, 2, 0, 9, 1);
    private final NumberValue axeSlot = new NumberValue("Axe Slot", this, 3, 0, 9, 1);
    private final NumberValue shovelSlot = new NumberValue("Shovel Slot", this, 4, 0, 9, 1);
    private final BoundsNumberValue blockSlot = new BoundsNumberValue("Block Slot", this, 5, 5, 0, 9, 1);
    private final BoundsNumberValue potionSlot = new BoundsNumberValue("Potion Slot", this, 6, 6, 0, 9, 1);
    private final NumberValue bowSlot = new NumberValue("Bow Slot", this, 7, 0, 9, 1);
    private final NumberValue add = new NumberValue("Rod Slot", this, 8, 0, 9, 1);
    private final BoundsNumberValue foodSlot = new BoundsNumberValue("Food Slot", this, 9, 9, 0, 9, 1);
    private static final int HELMET_SLOT = 39;
    private static final int CHESTPLATE_SLOT = 38;
    private static final int LEGGINGS_SLOT = 37;
    private static final int BOOTS_SLOT = 36;
    private int containerLookTicks = 0;
    private static final int HOTBAR_FIRST_SLOT = 0;
    private static final int HOTBAR_LAST_SLOT = 8;
    private static final int INVENTORY_FIRST_SLOT = 9;
    private static final int INVENTORY_LAST_SLOT = 35;
    private final StopWatch stopwatch = new StopWatch();
    private int chestTicks;
    private int attackTicks;
    private int placeTicks;
    private boolean moved;
    private boolean open;
    private long nextClick;
    private boolean pendingClose = false;
    private int clicksThisTick;
    private int sprintDisableTicks;
    private boolean inventoryOpen = false;
    private int openDelayTicks = 0;
    private int idleTicks = 0;
    @EventLink(value = 2)
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (aEg.thePlayer.ticksExisted > 40) {
                if (this.isLookingAtContainer(4.5)) {
                    this.containerLookTicks = 10;
                } else if (this.containerLookTicks > 0) {
                    this.containerLookTicks--;
                }

                if (this.pendingClose && aEg.currentScreen == null && this.open && !BadPacketsComponent.bad(false, false, false, false, true) && (this.containerLookTicks == 0 || this.placeTicks >= 10)) {
                    PacketUtil.send(new q(aEg.thePlayer.inventoryContainer.windowId));
                    this.registerClick();
                    this.pendingClose = false;
                    this.open = false;
                }

                if (this.isForeignContainerOpen()) {
                    this.chestTicks = 0;
                } else {
                    this.chestTicks++;
                }

                this.attackTicks++;
                this.placeTicks++;
                if (this.legit.wo() && !(aEg.currentScreen instanceof GuiInventory)) {
                    this.stopwatch.aX();
                    this.u(false);
                } else if (!this.stopwatch.T(this.nextClick) || this.chestTicks < 10 || this.attackTicks < 10 || this.placeTicks < 10 || this.e(Scaffold.class).isEnabled()) {
                    this.u(false);
                } else if (this.isForeignContainerOpen()) {
                    this.u(false);
                } else if (!this.e(InventoryMove.class).isEnabled() && !(aEg.currentScreen instanceof GuiInventory)) {
                    this.u(false);
                } else {
                    this.moved = false;
                    InventoryScan tr = this.scanInventory();
                    boolean flag = this.needsWork(tr);
                    if (!this.inventoryOpen) {
                        if (!flag) {
                            return;
                        }

                        this.openInventory();
                        if (this.openDelayTicks > 0) {
                            this.openDelayTicks--;
                            return;
                        }
                    } else {
                        if (!flag) {
                            this.u(true);
                            return;
                        }

                        if (this.openDelayTicks > 0) {
                            this.openDelayTicks--;
                            return;
                        }
                    }

                    if (this.clearCursorStack()) {
                        this.updateIdle();
                    } else if (this.swapArmor(0, tr.adF)) {
                        this.updateIdle();
                    } else if (this.swapArmor(1, tr.adG)) {
                        this.updateIdle();
                    } else if (this.swapArmor(2, tr.adH)) {
                        this.updateIdle();
                    } else if (this.swapArmor(3, tr.adI)) {
                        this.updateIdle();
                    } else if (this.moveTools(tr)) {
                        this.updateIdle();
                    } else if (this.moveBlocks(tr)) {
                        this.updateIdle();
                    } else if (this.movePotions(tr)) {
                        this.updateIdle();
                    } else if (this.moveFood(tr)) {
                        this.updateIdle();
                    } else if (this.dropExcess(var1x -> {
                        int k = 0;

                        for (int l = 0; l <= 39; l++) {
                            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                                var1x.add(l);
                                k += itemstack.stackSize;
                            }
                        }

                        return k;
                    }, this.blockLimit.wo().intValue())) {
                        this.updateIdle();
                    } else if (this.dropExcess(var1x -> {
                        int k = 0;

                        for (int l = 0; l <= 39; l++) {
                            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                            if (itemstack != null && itemstack.getItem() == Items.arrow) {
                                var1x.add(l);
                                k += itemstack.stackSize;
                            }
                        }

                        return k;
                    }, this.arrowLimit.wo().intValue())) {
                        this.updateIdle();
                    } else if (this.dropExcess(var1x -> {
                        int k = 0;

                        for (int l = 0; l <= 39; l++) {
                            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                            if (itemstack != null) {
                                Item item = itemstack.getItem();
                                if (item == Items.bucket || item == Items.water_bucket || item == Items.lava_bucket || item == Items.milk_bucket) {
                                    var1x.add(l);
                                    k += itemstack.stackSize;
                                }
                            }
                        }

                        return k;
                    }, this.bucketLimit.wo().intValue())) {
                        this.updateIdle();
                    } else if (this.dropExcess(var1x -> {
                        int k = 0;

                        for (int l = 0; l <= 39; l++) {
                            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                            if (itemstack != null) {
                                Item item = itemstack.getItem();
                                if (item == Items.snowball || item == Items.egg) {
                                    var1x.add(l);
                                    k += itemstack.stackSize;
                                }
                            }
                        }

                        return k;
                    }, this.snowballEggLimit.wo().intValue())) {
                        this.updateIdle();
                    } else if (this.dropExcess(var1x -> {
                        int k = 0;

                        for (int l = 0; l <= 39; l++) {
                            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                            if (itemstack != null && itemstack.getItem() == Items.ender_pearl) {
                                var1x.add(l);
                                k += itemstack.stackSize;
                            }
                        }

                        return k;
                    }, this.enderPearlLimit.wo().intValue())) {
                        this.updateIdle();
                    } else {
                        int i = this.firstUselessSlot();
                        if (i != -1 && this.canDrop(i, true)) {
                            this.dropStack(i);
                            this.updateIdle();
                        } else {
                            int j = this.h(tr);
                            if (j != -1 && this.canDrop(j, true)) {
                                this.dropStack(j);
                                this.updateIdle();
                            } else {
                                this.updateIdle();
                            }
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> this.attackTicks = 0;
    @EventLink
    public final Listener<SprintEvent> onSprint = var1 -> {
        if (this.isGrimInventoryMove() && this.clicksThisTick > 0) {
            this.sprintDisableTicks = Math.max(this.sprintDisableTicks, this.clicksThisTick + this.getExtraSprintTicks());
        }

        this.clicksThisTick = 0;
        if (this.isGrimInventoryMove() && this.sprintDisableTicks > 0) {
            aEg.thePlayer.setSprinting(false);
            this.sprintDisableTicks--;
        } else if (!this.isGrimInventoryMove()) {
            this.sprintDisableTicks = 0;
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

    public Manager() {
    }

    public boolean jJ() {
        return aEg != null
            && aEg.thePlayer != null
            && aEg.theWorld != null
            && this.stopwatch.T(this.nextClick)
            && this.chestTicks >= 10
            && this.attackTicks >= 10
            && this.placeTicks >= 10
            && (this.canOpenInventory() || aEg.currentScreen instanceof GuiInventory);
    }

    public int getBlockLimit() {
        return this.blockLimit.wo().intValue();
    }

    public int getArrowLimit() {
        return this.arrowLimit.wo().intValue();
    }

    public int getBucketLimit() {
        return this.bucketLimit.wo().intValue();
    }

    public int getSnowballEggLimit() {
        return this.snowballEggLimit.wo().intValue();
    }

    public int getEnderPearlLimit() {
        return this.enderPearlLimit.wo().intValue();
    }

    public boolean shouldDropCustomItems() {
        return this.dropCustomItems.wo();
    }

    private boolean isLookingAtContainer(double var1) {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            MovingObjectPosition movingobjectposition = aEg.thePlayer.rayTrace(var1, 1.0F);
            if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
                BlockPos blockpos = movingobjectposition.getBlockPos();
                Block block = aEg.theWorld.getBlockState(blockpos).getBlock();
                return block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockFurnace || block instanceof BlockWorkbench;
            }
            return false;
        }
        return false;
    }

    private void updateIdle() {
        if (this.moved) {
            this.idleTicks = 0;
        } else {
            this.idleTicks++;
            if (this.idleTicks >= 2) {
                this.u(true);
            }
        }
    }

    @Override
    public void onDisable() {
        this.u(false);
        this.clicksThisTick = 0;
        this.sprintDisableTicks = 0;
    }

    private InventoryScan scanInventory() {
        InventoryScan tr = new InventoryScan();
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        ArrayList arraylist6 = new ArrayList();
        ArrayList arraylist7 = new ArrayList();
        ArrayList arraylist8 = new ArrayList();
        ArrayList arraylist9 = new ArrayList();

        for (int i = 0; i <= 39; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
            if (itemstack != null) {
                Item item = itemstack.getItem();
                if (!ItemUtil.useful(itemstack)) {
                    tr.aef.add(i);
                } else {
                    if (item instanceof ItemBlock) {
                        tr.adQ.add(new SlotStack(i, itemstack));
                    }

                    if (item instanceof ItemPotion) {
                        tr.adR.add(new SlotStack(i, itemstack));
                    }

                    if (item instanceof ItemFood) {
                        tr.adS.add(new SlotStack(i, itemstack));
                    }

                    if (item instanceof ItemArmor) {
                        switch (((ItemArmor)item).armorType) {
                            case 0:
                                arraylist.add(i);
                                break;
                            case 1:
                                arraylist1.add(i);
                                break;
                            case 2:
                                arraylist2.add(i);
                                break;
                            case 3:
                                arraylist3.add(i);
                        }
                    } else if (item instanceof ItemSword) {
                        arraylist4.add(i);
                    } else if (item instanceof bw) {
                        arraylist5.add(i);
                    } else if (item instanceof ItemAxe) {
                        arraylist6.add(i);
                    } else if (item instanceof cn) {
                        arraylist7.add(i);
                    } else if (item instanceof ItemBow) {
                        arraylist8.add(i);
                    } else if (item instanceof be) {
                        arraylist9.add(i);
                    }
                }
            }
        }

        tr.adF = this.bestSlot(arraylist, tr.aeb, this::armorReduction);
        tr.adG = this.bestSlot(arraylist1, tr.aec, this::armorReduction);
        tr.adH = this.bestSlot(arraylist2, tr.aed, this::armorReduction);
        tr.adI = this.bestSlot(arraylist3, tr.aee, this::armorReduction);
        this.scanSwords(arraylist4, tr);
        tr.adL = this.bestSlot(arraylist5, tr.adU, var1 -> {
            Item item1 = var1.getItem();
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, var1);
            float f = j > 0 ? j * j + 1 : 0.0F;
            if (item1 instanceof bw) {
                return ((bw)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            } else if (item1 instanceof cn) {
                return ((cn)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            }
            return item1 instanceof ItemAxe ? ((ItemAxe)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f : 0.0F;
        });
        tr.adM = this.bestSlot(arraylist6, tr.adV, var1 -> {
            Item item1 = var1.getItem();
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, var1);
            float f = j > 0 ? j * j + 1 : 0.0F;
            if (item1 instanceof bw) {
                return ((bw)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            } else if (item1 instanceof cn) {
                return ((cn)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            }
            return item1 instanceof ItemAxe ? ((ItemAxe)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f : 0.0F;
        });
        tr.adN = this.bestSlot(arraylist7, tr.adW, var1 -> {
            Item item1 = var1.getItem();
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, var1);
            float f = j > 0 ? j * j + 1 : 0.0F;
            if (item1 instanceof bw) {
                return ((bw)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            } else if (item1 instanceof cn) {
                return ((cn)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f;
            }
            return item1 instanceof ItemAxe ? ((ItemAxe)item1).getToolMaterial().getEfficiencyOnProperMaterial() + f : 0.0F;
        });
        tr.adO = this.bestSlot(arraylist8, tr.adZ, var1 -> EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, var1));
        tr.adP = this.firstSlot(arraylist9, tr.aea);
        return tr;
    }

    private int bestSlot(List<Integer> var1, List<Integer> var2, ItemScorer var3) {
        if (var1.isEmpty()) {
            return -1;
        }

        int i = (Integer)var1.get(0);
        float f = var3.score(aEg.thePlayer.inventory.getStackInSlot(i));

        for (int j = 1; j < var1.size(); j++) {
            int k = (Integer)var1.get(j);
            float f1 = var3.score(aEg.thePlayer.inventory.getStackInSlot(k));
            if (f1 > f) {
                var2.add(i);
                i = k;
                f = f1;
            } else {
                var2.add(k);
            }
        }

        return i;
    }

    private void scanSwords(List<Integer> var1, InventoryScan var2) {
        var1.sort((var1x, var2x) -> Float.compare(this.damage(aEg.thePlayer.inventory.getStackInSlot(var2x)), this.damage(aEg.thePlayer.inventory.getStackInSlot(var1x))));
        if (!var1.isEmpty()) {
            var2.adJ = (Integer)var1.get(0);
            boolean flag = this.swordSlot.wo().intValue() != 0
                && this.secondSwordSlot.wo().intValue() != 0
                && this.secondSwordSlot.wo().intValue() != this.swordSlot.wo().intValue();
            byte b0 = 1;
            if (flag && var1.size() > 1) {
                var2.adK = (Integer)var1.get(1);
                b0 = 2;
            }

            for (int i = b0; i < var1.size(); i++) {
                var2.adT.add((Integer)var1.get(i));
            }
        }
    }

    private int firstSlot(List<Integer> var1, List<Integer> var2) {
        if (var1.isEmpty()) {
            return -1;
        }

        int i = (Integer)var1.get(0);

        for (int j = 1; j < var1.size(); j++) {
            var2.add((Integer)var1.get(j));
        }

        return i;
    }

    private boolean needsWork(InventoryScan var1) {
        if (aEg.thePlayer.inventory.getItemStack() != null) {
            return true;
        } else if (this.betterArmor(0, var1.adF)) {
            return true;
        } else if (this.betterArmor(1, var1.adG)) {
            return true;
        } else if (this.betterArmor(2, var1.adH)) {
            return true;
        } else if (this.betterArmor(3, var1.adI)) {
            return true;
        } else if (this.needsMove(var1.adJ, this.swordSlot)) {
            return true;
        } else if (this.needsMove(var1.adK, this.secondSwordSlot)) {
            return true;
        } else if (!this.slotTakenBySword(var1, this.pickaxeSlot) && this.needsMove(var1.adL, this.pickaxeSlot)) {
            return true;
        } else if (!this.slotTakenBySword(var1, this.axeSlot) && this.needsMove(var1.adM, this.axeSlot)) {
            return true;
        } else if (!this.slotTakenBySword(var1, this.shovelSlot) && this.needsMove(var1.adN, this.shovelSlot)) {
            return true;
        } else if (!this.slotTakenBySword(var1, this.bowSlot) && this.needsMove(var1.adO, this.bowSlot)) {
            return true;
        }
        int i = this.add.wo().intValue();
        int j = this.bowSlot.wo().intValue();
        if (!this.slotTakenBySword(var1, this.add) && i != 0 && (j == 0 || i != j || var1.adO == -1) && this.needsMove(var1.adP, this.add)) {
            return true;
        } else if (this.needsMoveRange(var1.adQ, this.blockSlot)) {
            return true;
        } else if (this.needsMovePotion(var1)) {
            return true;
        } else if (this.needsMoveFood(var1)) {
            return true;
        } else if (this.overLimit(var1x -> {
            int k = 0;

            for (int l = 0; l <= 39; l++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                    var1x.add(l);
                    k += itemstack.stackSize;
                }
            }

            return k;
        }, this.blockLimit.wo().intValue())) {
            return true;
        } else if (this.overLimit(var1x -> {
            int k = 0;

            for (int l = 0; l <= 39; l++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                if (itemstack != null && itemstack.getItem() == Items.arrow) {
                    var1x.add(l);
                    k += itemstack.stackSize;
                }
            }

            return k;
        }, this.arrowLimit.wo().intValue())) {
            return true;
        } else if (this.overLimit(var1x -> {
            int k = 0;

            for (int l = 0; l <= 39; l++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                if (itemstack != null) {
                    Item item = itemstack.getItem();
                    if (item == Items.bucket || item == Items.water_bucket || item == Items.lava_bucket || item == Items.milk_bucket) {
                        var1x.add(l);
                        k += itemstack.stackSize;
                    }
                }
            }

            return k;
        }, this.bucketLimit.wo().intValue())) {
            return true;
        } else if (this.overLimit(var1x -> {
            int k = 0;

            for (int l = 0; l <= 39; l++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                if (itemstack != null) {
                    Item item = itemstack.getItem();
                    if (item == Items.snowball || item == Items.egg) {
                        var1x.add(l);
                        k += itemstack.stackSize;
                    }
                }
            }

            return k;
        }, this.snowballEggLimit.wo().intValue())) {
            return true;
        } else if (this.overLimit(var1x -> {
            int k = 0;

            for (int l = 0; l <= 39; l++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
                if (itemstack != null && itemstack.getItem() == Items.ender_pearl) {
                    var1x.add(l);
                    k += itemstack.stackSize;
                }
            }

            return k;
        }, this.enderPearlLimit.wo().intValue())) {
            return true;
        } else {
            return this.firstUselessSlot() != -1
                ? true
                : !var1.adT.isEmpty()
                    || !var1.adU.isEmpty()
                    || !var1.adV.isEmpty()
                    || !var1.adW.isEmpty()
                    || !var1.adZ.isEmpty()
                    || !var1.aea.isEmpty()
                    || !var1.aeb.isEmpty()
                    || !var1.aec.isEmpty()
                    || !var1.aed.isEmpty()
                    || !var1.aee.isEmpty();
        }
    }

    private boolean betterArmor(int var1, int var2) {
        if (var2 == -1) {
            return false;
        }
        int i = this.armorSlot(var1);
        ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
        ItemStack itemstack1 = aEg.thePlayer.inventory.getStackInSlot(var2);
        if (itemstack1 != null && itemstack1.getItem() instanceof ItemArmor) {
            int j = this.armorReduction(itemstack1);
            int k = itemstack != null && itemstack.getItem() instanceof ItemArmor ? this.armorReduction(itemstack) : Integer.MIN_VALUE;
            return var2 == i ? false : j > k;
        }
        return false;
    }

    private boolean needsMove(Integer var1, NumberValue numberValue) {
        if (numberValue.wo().intValue() != 0 && var1 != null && var1 != -1) {
            int i = numberValue.wo().intValue() - 1;
            return !this.isInSlot(var1, i);
        }
        return false;
    }

    private boolean slotTakenBySword(InventoryScan var1, NumberValue numberValue) {
        int i = numberValue.wo().intValue();
        return i != 0 && (var1.adJ != -1 && this.swordSlot.wo().intValue() == i || var1.adK != -1 && this.secondSwordSlot.wo().intValue() == i);
    }

    private boolean needsMoveRange(List<SlotStack> var1, BoundsNumberValue boundsNumberValue) {
        int i = boundsNumberValue.wo().intValue();
        int j = boundsNumberValue.wA().intValue();
        if (i == 0 || i > j) {
            return false;
        }

        if (var1.isEmpty()) {
            return false;
        }

        var1.sort(Comparator.comparingInt(var0 -> -var0.aeh.stackSize));
        int k = j - i + 1;

        for (int l = 0; l < k && l < var1.size(); l++) {
            int i1 = i - 1 + l;
            int j1 = ((SlotStack)var1.get(l)).aeg;
            if (!this.isInSlot(j1, i1)) {
                return true;
            }
        }

        return false;
    }

    private boolean needsMovePotion(InventoryScan var1) {
        int i = this.potionSlot.wo().intValue();
        int j = this.potionSlot.wA().intValue();
        if (i != 0 && i <= j) {
            ArrayList arraylist = new ArrayList();

            for (SlotStack ts : var1.adR) {
                List list = ((ItemPotion)ts.aeh.getItem()).getEffects(ts.aeh);
                if (list != null && !list.isEmpty()) {
                    arraylist.add(ts);
                }
            }

            if (arraylist.isEmpty()) {
                return false;
            }

            arraylist.sort((var1x, var2) -> {
                boolean flag = ItemPotion.isSplash(((SlotStack)var1x).aeh.getMetadata());
                boolean metadata = ItemPotion.isSplash(((SlotStack)var2).aeh.getMetadata());
                if (this.prioritizeSplashPotions.wo()) {
                    if (flag && !metadata) {
                        return -1;
                    }

                    if (!flag && metadata) {
                        return 1;
                    }
                }

                int k1 = PlayerUtil.potionRanking(((ItemPotion)((SlotStack)var1x).aeh.getItem()).getEffects(((SlotStack)var1x).aeh).get(0).getPotionID());
                return Integer.compare(PlayerUtil.potionRanking(((ItemPotion)((SlotStack)var2).aeh.getItem()).getEffects(((SlotStack)var2).aeh).get(0).getPotionID()), k1);
            });
            int k = j - i + 1;

            for (int l = 0; l < k && l < arraylist.size(); l++) {
                int i1 = i - 1 + l;
                int j1 = ((SlotStack)arraylist.get(l)).aeg;
                if (!this.isInSlot(j1, i1)) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private boolean needsMoveFood(InventoryScan var1) {
        int i = this.foodSlot.wo().intValue();
        int j = this.foodSlot.wA().intValue();
        if (i == 0 || i > j) {
            return false;
        }

        if (var1.adS.isEmpty()) {
            return false;
        }

        var1.adS
            .sort(
                (var0, var1x) -> Float.compare(
                    ((ItemFood)var1x.aeh.getItem()).getSaturationModifier(var1x.aeh), ((ItemFood)var0.aeh.getItem()).getSaturationModifier(var0.aeh)
                )
            );
        int k = j - i + 1;

        for (int l = 0; l < k && l < var1.adS.size(); l++) {
            int i1 = i - 1 + l;
            int j1 = var1.adS.get(l).aeg;
            if (!this.isInSlot(j1, i1)) {
                return true;
            }
        }

        return false;
    }

    private boolean overLimit(IntGatherer intGatherer, int var2) {
        ArrayList arraylist = new ArrayList();
        return intGatherer.gather(arraylist) > var2;
    }

    private boolean swapArmor(int var1, int var2) {
        if (var2 == -1) {
            return false;
        }

        int i = this.armorSlot(var1);
        ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
        ItemStack itemstack1 = aEg.thePlayer.inventory.getStackInSlot(var2);
        if (itemstack1 != null && itemstack1.getItem() instanceof ItemArmor) {
            int j = this.armorReduction(itemstack1);
            int k = itemstack != null && itemstack.getItem() instanceof ItemArmor ? this.armorReduction(itemstack) : Integer.MIN_VALUE;
            if (var2 != i && j > k) {
                int l = aEg.thePlayer.inventoryContainer.windowId;
                int i1 = this.slot(var2);
                int j1 = this.armorWindowSlot(var1);
                if (aEg.thePlayer.inventory.getItemStack() != null) {
                    return false;
                }

                if (ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_12)) {
                    this.windowClick(l, i1, 0, 0);
                    this.windowClick(l, j1, 0, 0);
                    this.windowClick(l, i1, 0, 0);
                } else {
                    this.windowClick(l, i1, 1, 1);
                    this.windowClick(l, j1, 1, 1);
                    this.windowClick(l, i1, 1, 1);
                }

                this.throwItem();
                return true;
            }
            return false;
        }
        return false;
    }

    private int armorSlot(int var1) {
        switch (var1) {
            case 0:
                return 39;
            case 1:
                return 38;
            case 2:
                return 37;
            case 3:
                return 36;
            default:
                return 39;
        }
    }

    private int armorWindowSlot(int var1) {
        switch (var1) {
            case 0:
                return 5;
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 8;
            default:
                return 5;
        }
    }

    private boolean moveTools(InventoryScan var1) {
        if (this.moveToSlot(var1.adJ, this.swordSlot)) {
            return true;
        }

        if (this.moveToSlot(var1.adK, this.secondSwordSlot)) {
            return true;
        }

        if (!this.slotTakenBySword(var1, this.pickaxeSlot) && this.moveToSlot(var1.adL, this.pickaxeSlot)) {
            return true;
        }

        if (!this.slotTakenBySword(var1, this.axeSlot) && this.moveToSlot(var1.adM, this.axeSlot)) {
            return true;
        }

        if (!this.slotTakenBySword(var1, this.shovelSlot) && this.moveToSlot(var1.adN, this.shovelSlot)) {
            return true;
        }

        if (!this.slotTakenBySword(var1, this.bowSlot) && this.moveToSlot(var1.adO, this.bowSlot)) {
            return true;
        }

        int i = this.add.wo().intValue();
        int j = this.bowSlot.wo().intValue();
        return !this.slotTakenBySword(var1, this.add) && i != 0 && (j == 0 || i != j || var1.adO == -1) && this.moveToSlot(var1.adP, this.add);
    }

    private boolean moveBlocks(InventoryScan var1) {
        int i = this.blockSlot.wo().intValue();
        int j = this.blockSlot.wA().intValue();
        if (i != 0 && i <= j) {
            var1.adQ.sort(Comparator.comparingInt(var0 -> -var0.aeh.stackSize));
            int k = j - i + 1;

            for (int l = 0; l < k && l < var1.adQ.size(); l++) {
                int i1 = i - 1 + l;
                int j1 = var1.adQ.get(l).aeg;
                if (!this.isInSlot(j1, i1) && this.canClick(j1, false)) {
                    this.swapToHotbar(j1, i1);
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private boolean movePotions(InventoryScan var1) {
        int i = this.potionSlot.wo().intValue();
        int j = this.potionSlot.wA().intValue();
        if (i != 0 && i <= j) {
            ArrayList arraylist = new ArrayList();

            for (SlotStack ts : var1.adR) {
                List list = ((ItemPotion)ts.aeh.getItem()).getEffects(ts.aeh);
                if (list != null && !list.isEmpty()) {
                    arraylist.add(ts);
                }
            }

            arraylist.sort((var1x, var2) -> {
                boolean flag = ItemPotion.isSplash(((SlotStack)var1x).aeh.getMetadata());
                boolean metadata = ItemPotion.isSplash(((SlotStack)var2).aeh.getMetadata());
                if (this.prioritizeSplashPotions.wo()) {
                    if (flag && !metadata) {
                        return -1;
                    }

                    if (!flag && metadata) {
                        return 1;
                    }
                }

                int k1 = PlayerUtil.potionRanking(((ItemPotion)((SlotStack)var1x).aeh.getItem()).getEffects(((SlotStack)var1x).aeh).get(0).getPotionID());
                return Integer.compare(PlayerUtil.potionRanking(((ItemPotion)((SlotStack)var2).aeh.getItem()).getEffects(((SlotStack)var2).aeh).get(0).getPotionID()), k1);
            });
            int k = j - i + 1;

            for (int l = 0; l < k && l < arraylist.size(); l++) {
                int i1 = i - 1 + l;
                int j1 = ((SlotStack)arraylist.get(l)).aeg;
                if (!this.isInSlot(j1, i1) && this.canClick(j1, false)) {
                    this.swapToHotbar(j1, i1);
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private boolean moveFood(InventoryScan var1) {
        int i = this.foodSlot.wo().intValue();
        int j = this.foodSlot.wA().intValue();
        if (i != 0 && i <= j) {
            var1.adS
                .sort(
                    (var0, var1x) -> Float.compare(
                        ((ItemFood)var1x.aeh.getItem()).getSaturationModifier(var1x.aeh), ((ItemFood)var0.aeh.getItem()).getSaturationModifier(var0.aeh)
                    )
                );
            int k = j - i + 1;

            for (int l = 0; l < k && l < var1.adS.size(); l++) {
                int i1 = i - 1 + l;
                int j1 = var1.adS.get(l).aeg;
                if (!this.isInSlot(j1, i1) && this.canClick(j1, false)) {
                    this.swapToHotbar(j1, i1);
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private boolean dropExcess(IntGatherer intGatherer, int var2) {
        ArrayList arraylist = new ArrayList();
        int i = intGatherer.gather(arraylist);
        if (i <= var2) {
            return false;
        }

        int j = i - var2;

        for (int k = arraylist.size() - 1; k >= 0; k--) {
            int l = (Integer)arraylist.get(k);
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(l);
            if (itemstack != null) {
                int stackSize = itemstack.stackSize;
                if (this.canDrop(l, true)) {
                    if (j >= stackSize) {
                        this.dropStack(l);
                        return true;
                    }

                    int j1 = stackSize - j;
                    this.dropPartialStack(l, j1);
                    return true;
                }
            }
        }

        return false;
    }

    private int firstUselessSlot() {
        for (int i = 0; i <= 39; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
            if (itemstack != null && !ItemUtil.useful(itemstack)) {
                return i;
            }
        }

        return -1;
    }

    private int h(InventoryScan var1) {
        for (int i : var1.adT) {
            if (this.canDrop(i, true)) {
                return i;
            }
        }

        for (int j : var1.adU) {
            if (this.canDrop(j, true)) {
                return j;
            }
        }

        for (int k : var1.adV) {
            if (this.canDrop(k, true)) {
                return k;
            }
        }

        for (int l : var1.adW) {
            if (this.canDrop(l, true)) {
                return l;
            }
        }

        for (int i1 : var1.adZ) {
            if (this.canDrop(i1, true)) {
                return i1;
            }
        }

        for (int j1 : var1.aea) {
            if (this.canDrop(j1, true)) {
                return j1;
            }
        }

        for (int k1 : var1.aeb) {
            if (this.canDrop(k1, true)) {
                return k1;
            }
        }

        for (int l1 : var1.aec) {
            if (this.canDrop(l1, true)) {
                return l1;
            }
        }

        for (int i2 : var1.aed) {
            if (this.canDrop(i2, true)) {
                return i2;
            }
        }

        for (int j2 : var1.aee) {
            if (this.canDrop(j2, true)) {
                return j2;
            }
        }

        return -1;
    }

    private void openInventory() {
        this.idleTicks = 0;
        if (!this.isForeignContainerOpen()) {
            if (aEg.currentScreen instanceof GuiInventory) {
                this.inventoryOpen = true;
                this.openDelayTicks = 0;
            } else {
                if (this.canOpenInventory() && !this.open && aEg.currentScreen == null && !BadPacketsComponent.bad(false, false, false, false, true)) {
                    PacketUtil.send(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.registerClick();
                    this.open = true;
                    this.inventoryOpen = true;
                    this.openDelayTicks = 1;
                }
            }
        }
    }

    private void u(boolean var1) {
        if (this.inventoryOpen) {
            if (this.open) {
                if (var1 && aEg.currentScreen == null && !BadPacketsComponent.bad(false, false, false, false, true)) {
                    this.open = false;
                } else {
                    this.pendingClose = true;
                }
            }

            this.inventoryOpen = false;
            this.openDelayTicks = 0;
            this.idleTicks = 0;
        }
    }

    private boolean canOpenInventory() {
        boolean flag = this.e(InventoryMove.class).isEnabled();
        boolean flag1 = aEg.currentScreen == null;
        boolean flag2 = this.e(Scaffold.class).isEnabled();
        return flag && flag1 && !flag2 && !aEg.gameSettings.cgK.isKeyDown() && !aEg.gameSettings.cgI.isKeyDown();
    }

    private boolean isForeignContainerOpen() {
        return aEg.currentScreen instanceof GuiContainer && !(aEg.currentScreen instanceof GuiInventory);
    }

    private boolean clearCursorStack() {
        if (aEg.thePlayer.inventory.getItemStack() == null) {
            return false;
        }

        int i = aEg.thePlayer.inventoryContainer.windowId;

        for (int j = 0; j <= 39; j++) {
            if (j != 36 && j != 37 && j != 38 && j != 39 && aEg.thePlayer.inventory.getStackInSlot(j) == null) {
                this.windowClick(i, this.slot(j), 0, 0);
                this.throwItem();
                return true;
            }
        }

        this.windowClick(i, -999, 0, 0);
        this.throwItem();
        return true;
    }

    private void dropStack(int var1) {
        if (this.canDrop(var1, true)) {
            int i = aEg.thePlayer.inventoryContainer.windowId;
            int j = this.slot(var1);
            this.windowClick(i, j, 1, 4);
            this.throwItem();
        }
    }

    private void dropPartialStack(int var1, int var2) {
        if (this.canDrop(var1, true) && var2 >= 0) {
            int i = aEg.thePlayer.inventoryContainer.windowId;
            int j = this.slot(var1);
            this.windowClick(i, j, 0, 0);

            for (int k = 0; k < var2; k++) {
                this.windowClick(i, j, 1, 0);
            }

            this.windowClick(i, -999, 0, 0);
            this.throwItem();
        }
    }

    private void shiftClickSlot(int var1) {
        if (this.canClick(var1, false)) {
            int i = aEg.thePlayer.inventoryContainer.windowId;
            int j = this.slot(var1);
            this.windowClick(i, j, 0, 1);
            this.throwItem();
        }
    }

    private void swapToHotbar(int var1, int var2) {
        if (this.canClick(var1, false) && var2 >= 0 && var2 <= 8) {
            int i = aEg.thePlayer.inventoryContainer.windowId;
            int j = this.slot(var1);
            this.windowClick(i, j, var2, 2);
            this.throwItem();
        }
    }

    private boolean moveToSlot(Integer var1, NumberValue numberValue) {
        if (numberValue.wo().intValue() != 0 && var1 != null && var1 != -1) {
            int i = numberValue.wo().intValue() - 1;
            if (this.isInSlot(var1, i)) {
                return false;
            }

            if (!this.canClick(var1, false)) {
                return false;
            }

            this.swapToHotbar(var1, i);
            return true;
        }
        return false;
    }

    private boolean isInSlot(int var1, int var2) {
        return var1 >= 0 && var1 <= 8 && var1 == var2;
    }

    private void windowClick(int var1, int var2, int var3, int var4) {
        aEg.playerController.windowClick(var1, var2, var3, var4, aEg.thePlayer);
        this.registerClick();
    }

    private void registerClick() {
        if (this.isGrimInventoryMove()) {
            this.clicksThisTick++;
        }
    }

    private boolean isGrimInventoryMove() {
        InventoryMove inventorymove = this.e(InventoryMove.class);
        return inventorymove != null && inventorymove.isEnabled() && inventorymove.isGrimBypass();
    }

    private int getExtraSprintTicks() {
        InventoryMove inventorymove = this.e(InventoryMove.class);
        return inventorymove == null ? 9 : inventorymove.getExtraSprintTicks();
    }

    private void throwItem() {
        this.nextClick = Math.round(MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue()));
        this.stopwatch.aX();
        this.moved = true;
        this.idleTicks = 0;
    }

    private boolean canClick(int var1, boolean var2) {
        if (this.moved && this.nextClick > 0L) {
            return false;
        }

        boolean flag = var2 ? !this.dropCustomItems.wo() : !this.useCustomItems.wo();
        return !SelectorDetectionComponent.a(var1, flag);
    }

    private boolean canDrop(int var1, boolean var2) {
        return this.canClick(var1, true);
    }

    private float damage(ItemStack stack) {
        ItemSword itemsword = (ItemSword)stack.getItem();
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
        return (float)(itemsword.getDamageVsEntity() + i * 1.25 + j * 2.5);
    }

    private int armorReduction(ItemStack stack) {
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

    private int slot(int var1) {
        if (var1 >= 0 && var1 <= 8) {
            return 36 + var1;
        } else if (var1 >= 9 && var1 <= 35) {
            return var1;
        } else if (var1 == 39) {
            return 5;
        } else if (var1 == 38) {
            return 6;
        } else if (var1 == 37) {
            return 7;
        }
        return var1 == 36 ? 8 : var1;
    }

    @Generated
    public boolean isMoved() {
        return this.moved;
    }

    @Generated
    public boolean kb() {
        return this.open;
    }
}
