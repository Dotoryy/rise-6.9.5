package com.alan.clients.util.player;

import com.alan.clients.util.interfaces.InstanceAccess;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class SlotUtil implements InstanceAccess {
    public static final List<Block> blacklist = Arrays.asList(
        Blocks.enchanting_table,
        Blocks.chest,
        Blocks.ender_chest,
        Blocks.trapped_chest,
        Blocks.anvil,
        Blocks.sand,
        Blocks.web,
        Blocks.torch,
        Blocks.crafting_table,
        Blocks.furnace,
        Blocks.waterlily,
        Blocks.dispenser,
        Blocks.stone_pressure_plate,
        Blocks.wooden_pressure_plate,
        Blocks.noteblock,
        Blocks.dropper,
        Blocks.tnt,
        Blocks.standing_banner,
        Blocks.wall_banner,
        Blocks.redstone_torch,
        Blocks.pumpkin
    );
    public static final List<Block> interactList = Arrays.asList(
        Blocks.enchanting_table,
        Blocks.chest,
        Blocks.ender_chest,
        Blocks.trapped_chest,
        Blocks.anvil,
        Blocks.crafting_table,
        Blocks.furnace,
        Blocks.dispenser,
        Blocks.iron_door,
        Blocks.oak_door,
        Blocks.noteblock,
        Blocks.dropper
    );

    public static int vx() {
        for (int i = 36; i < 45; i++) {
            ItemStack itemstack = aEg.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemstack != null) {
                boolean flag = itemstack.getItem() instanceof ItemBlock && (itemstack.cWo > 5 || itemstack.stackSize > 20);
                boolean flag1 = itemstack.hasDisplayName() && itemstack.getDisplayName().contains("Black Hole");
                if (flag) {
                    if (itemstack.getItem() instanceof ItemBlock) {
                        Block block = ((ItemBlock)itemstack.getItem()).getBlock();
                        if (block.isFullBlock()
                            || block instanceof BlockGlass
                            || block instanceof BlockStainedGlass
                            || block instanceof BlockTNT
                            || block instanceof BlockEndPortalFrame) {
                            return i - 36;
                        }
                    } else if (flag1) {
                        return i - 36;
                    }
                }
            }
        }

        return -1;
    }

    public static int vy() {
        int i = -1;
        float f = -1.0F;
        int j = -1;

        for (int k = 0; k < 9; k++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(k);
            if (itemstack != null && itemstack.getItem() instanceof ItemSword) {
                ItemSword itemsword = (ItemSword)itemstack.getItem();
                int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemstack);
                float f1 = itemsword.getDamageVsEntity() + l * 1.25F;
                int maxDamage = itemsword.getMaxDamage();
                if (f < f1) {
                    f = f1;
                    i = maxDamage;
                    j = k;
                }

                if (f1 == f && maxDamage > i) {
                    i = maxDamage;
                    j = k;
                }
            }
        }

        return j;
    }

    public static int findItem(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
            if (itemstack == null) {
                if (item == null) {
                    return i;
                }
            } else if (itemstack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }

    public static int findBlock(Block block) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
            if (itemstack == null) {
                if (block == null) {
                    return i;
                }
            } else if (itemstack.getItem() instanceof ItemBlock && ((ItemBlock)itemstack.getItem()).getBlock() == block) {
                return i;
            }
        }

        return -1;
    }

    public static int findTool(BlockPos pos) {
        float f = 1.0F;
        int i = -1;
        IBlockState iblockstate = aEg.theWorld.getBlockState(pos);

        for (int j = 0; j < 9; j++) {
            ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(j);
            if (itemstack != null) {
                float block = itemstack.getStrVsBlock(iblockstate.getBlock());
                if (block > f) {
                    f = block;
                    i = j;
                }
            }
        }

        return i;
    }

    public static ItemStack getCurrentItemInSlot(int var0) {
        return var0 < 9 && var0 >= 0 ? aEg.thePlayer.inventory.mainInventory[var0] : null;
    }

    public static float getStrVsBlock(Block block, int var1) {
        float f = 1.0F;
        if (aEg.thePlayer.inventory.mainInventory[var1] != null) {
            f *= aEg.thePlayer.inventory.mainInventory[var1].getStrVsBlock(block);
        }

        return f;
    }

    public static float getPlayerRelativeBlockHardness(EntityPlayer player, World world, BlockPos pos, int var3) {
        Block block = aEg.theWorld.getBlockState(pos).getBlock();
        float f = block.getBlockHardness(world, pos);
        return f < 0.0F ? 0.0F : (!canHeldItemHarvest(block, var3) ? getToolDigEfficiency(block, var3) / f / 100.0F : getToolDigEfficiency(block, var3) / f / 30.0F);
    }

    public static boolean canHeldItemHarvest(Block block, int var1) {
        if (block.getMaterial().isToolNotRequired()) {
            return true;
        }

        ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(var1);
        return itemstack != null && itemstack.canHarvestBlock(block);
    }

    public static float a(World world, Block block, BlockPos pos, int var3, Item item, boolean var5) {
        int i = 1;
        boolean flag = false;
        if (item == null) {
            i = 1;
            flag = true;
        }

        if (!flag) {
            int j = Item.getIdFromItem(item);
            if (j == 269 || j == 270 || j == 271) {
                i = 2;
            }

            if (j == 273 || j == 274 || j == 275) {
                i = 4;
            }

            if (j == 256 || j == 257 || j == 258) {
                i = 6;
            }

            if (j == 277 || j == 278 || j == 279) {
                i = 8;
            }

            if (j == 284 || j == 285 || j == 286) {
                i = 12;
            }

            if ((j == 267 || j == 268 || j == 272 || j == 276 || j == 283) && block instanceof BlockWeb) {
                i = 1;
            }

            if (j == 359 && block instanceof BlockVine) {
                i = 1;
            } else if (j != 359 || !(block instanceof BlockWeb) && !(block instanceof BlockLeaves)) {
                if (j == 359 && block.getBlockHardness(Minecraft.getMinecraft().theWorld, pos) == 0.8) {
                    i = 5;
                }
            } else {
                i = 15;
            }

            if (!canHeldItemHarvest(block, var3)) {
                i = 1;
            } else if (EnchantmentHelper.getEfficiencyModifier(Minecraft.getMinecraft().thePlayer) != 0) {
                i += EnchantmentHelper.getEfficiencyModifier(Minecraft.getMinecraft().thePlayer) ^ 3;
            }
        }

        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.digSpeed)) {
            i = (int)(i * (0.2 * Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1.0));
        }

        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.digSlowdown)) {
            i = (int)(i * switch (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            });
        }

        if (Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.water)
            && !EnchantmentHelper.getAquaAffinityModifier(Minecraft.getMinecraft().thePlayer)) {
            i = (int)(i / 5.0F);
        }

        if (!var5) {
            i = (int)(i / 5.0F);
        }

        float f = i / block.getBlockHardness(world, pos);
        float f1;
        if (canHeldItemHarvest(block, var3)) {
            f1 = f / 30.0F;
        } else {
            f1 = f / 100.0F;
        }

        return f1 > 1.0F ? 0.0F : (int)Math.ceil(1.0F / f1);
    }

    public static float getToolDigEfficiency(Block block, int var1) {
        float f = getStrVsBlock(block, var1);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(aEg.thePlayer);
            ItemStack itemstack = getCurrentItemInSlot(var1);
            if (i > 0 && itemstack != null) {
                f += i * i + 1;
            }
        }

        if (aEg.thePlayer.isPotionActive(Potion.digSpeed)) {
            f *= 1.0F + (aEg.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (aEg.thePlayer.isPotionActive(Potion.digSlowdown)) {
            f *= switch (aEg.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
        }

        if (aEg.thePlayer.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(aEg.thePlayer)) {
            f /= 5.0F;
        }

        if (!aEg.thePlayer.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    @Generated
    private SlotUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
