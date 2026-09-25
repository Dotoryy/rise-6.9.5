package com.alan.clients.util.player;

import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ax;
import net.minecraft.item.bw;
import net.minecraft.item.cn;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public final class ItemUtil {
    private static final List<Item> WHITELISTED_ITEMS = Arrays.asList(Items.fishing_rod, Items.water_bucket, Items.bucket, Items.arrow, Items.bow, Items.snowball, Items.egg, Items.ender_pearl, Items.clay_ball, Items.fire_charge);

    public static boolean useful(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemPotion) {
            ItemPotion itemPotion = (ItemPotion)item;
        }
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).getBlock();
            if (block instanceof BlockTNT) {
                return true;
            }
            if (block instanceof BlockGlass) return true;
            if (block instanceof BlockStainedGlass) return true;
            if (!(!block.isFullBlock() || block instanceof ITileEntityProvider || block instanceof BlockContainer || block instanceof BlockTNT || block instanceof BlockSlime || block instanceof BlockFalling)) {
                return true;
            }
        }
        if (item instanceof ItemSword) return true;
        if (item instanceof ItemTool) return true;
        if (item instanceof ItemArmor) return true;
        if (item instanceof ItemFood) return true;
        if (item instanceof ItemPotion) return true;
        if (!WHITELISTED_ITEMS.contains(item)) return false;
        return true;
    }

    public static boolean a(ItemStack itemStack, Container container) {
        Item item = itemStack.getItem();
        if (item instanceof ItemSword) {
            return ItemUtil.b(itemStack, container);
        }
        if (item instanceof ItemTool) {
            return ItemUtil.b(itemStack, container, ItemUtil.d(item));
        }
        if (item instanceof ItemBlock var6_3) {
            return var6_3.getBlock().isFullBlock();
        }
        if (item instanceof ItemBucketMilk) return true;
        if (item instanceof ItemBucket) return true;
        if (item instanceof ax) {
            return true;
        }
        if (item instanceof ItemPotion) {
            ItemPotion itemPotion = (ItemPotion)item;
            List<PotionEffect> list = itemPotion.getEffects(itemStack);
            if (list == null) return false;
            return list.stream().anyMatch(potionEffect -> {
                if (Potion.potionTypes[potionEffect.getPotionID()].isBadEffect()) return false;
                return true;
            });
        }
        if (item instanceof ItemArmor var9_6) {
            return ItemUtil.a(itemStack, container, var9_6.armorType);
        }
        if (item instanceof ItemFood) {
            return true;
        }
        if (item instanceof ItemBow) {
            return ItemUtil.c(itemStack, container);
        }
        if (item != Items.arrow) return false;
        return true;
    }

    public static boolean a(ItemStack itemStack, Container container, int n2) {
        ItemStack itemStack2 = ItemUtil.c(container, n2);
        if (itemStack2 == null) return true;
        if (itemStack == itemStack2) return true;
        if (!(ItemUtil.x(itemStack) > ItemUtil.x(itemStack2))) return false;
        return true;
    }

    public static boolean b(ItemStack itemStack, Container container, int n2) {
        ItemStack itemStack2 = ItemUtil.b(container, n2);
        if (itemStack2 == null) return true;
        if (itemStack == itemStack2) return true;
        if (!(ItemUtil.v(itemStack) > ItemUtil.v(itemStack2))) return false;
        return true;
    }

    public static boolean b(ItemStack itemStack, Container container) {
        ItemStack itemStack2 = ItemUtil.b(container);
        if (itemStack2 == null) return true;
        if (itemStack == itemStack2) return true;
        if (!(ItemUtil.w(itemStack) > ItemUtil.w(itemStack2))) return false;
        return true;
    }

    public static boolean c(ItemStack itemStack, Container container) {
        ItemStack itemStack2 = ItemUtil.c(container);
        if (itemStack2 == null) return true;
        if (itemStack == itemStack2) return true;
        if (ItemUtil.y(itemStack) <= ItemUtil.y(itemStack2)) return false;
        return true;
    }

    private static ItemStack b(Container container, int n2) {
        return ItemUtil.a(9, 45, container, itemStack -> {
            if (itemStack.getItem() == null) return false;
            if (!(itemStack.getItem() instanceof ItemTool)) return false;
            if (ItemUtil.d((ItemTool)itemStack.getItem()) != n2) return false;
            return true;
        }, Comparator.comparingDouble(ItemUtil::v));
    }

    private static ItemStack b(Container container) {
        return ItemUtil.a(9, 45, container, itemStack -> itemStack.getItem() instanceof ItemSword, Comparator.comparingDouble(ItemUtil::w));
    }

    private static ItemStack c(Container container, int n2) {
        return ItemUtil.a(5, 45, container, itemStack -> {
            if (itemStack.getItem() == null) return false;
            if (!(itemStack.getItem() instanceof ItemArmor)) return false;
            if (((ItemArmor)itemStack.getItem()).armorType != n2) return false;
            return true;
        }, Comparator.comparingDouble(ItemUtil::x));
    }

    private static ItemStack c(Container container) {
        return ItemUtil.a(9, 45, container, itemStack -> itemStack.getItem() instanceof ItemBow, Comparator.comparingDouble(ItemUtil::y));
    }

    private static ItemStack a(int n3, int n4, Container container, Predicate<ItemStack> predicate, Comparator<ItemStack> comparator) {
        return IntStream.range(n3, n4).mapToObj(n2 -> container.getSlot(n2).getStack()).filter(itemStack -> {
            if (itemStack == null) return false;
            if (!predicate.test(itemStack)) return false;
            return true;
        }).max(comparator.thenComparingDouble(itemStack -> itemStack.getMaxDamage() - itemStack.getItemDamage())).orElse(null);
    }

    private static float v(ItemStack itemStack) {
        int n2;
        float f2 = ((ItemTool)itemStack.getItem()).getToolMaterial().getEfficiencyOnProperMaterial();
        if (f2 > 1.0f && (n2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack)) > 0) {
            f2 += (float)(n2 * n2 + 1);
        }
        return f2;
    }

    private static double w(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemSword) {
            ItemSword itemSword = (ItemSword)item;
            int n2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
            int n3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);
            return (double)itemSword.getDamageVsEntity() + (double)n2 * 1.25 + (double)n3 * 2.5;
        }
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (multimap.isEmpty()) return 0.0;
        Iterator<Map.Entry<String, AttributeModifier>> iterator = multimap.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            if (!entry.getKey().equals(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())) continue;
            AttributeModifier attributeModifier = entry.getValue();
            double d2 = attributeModifier.getAmount() + (double)EnchantmentHelper.a(itemStack, EnumCreatureAttribute.UNDEFINED);
            double d3 = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? d2 : d2 * 100.0;
            if (d2 > 0.0) {
                return d3;
            }
            if (d2 < 0.0) return d3 * -1.0;
        }
        return 0.0;
    }

    private static double x(ItemStack itemStack) {
        ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        float f2 = -1.0f;
        int n2 = 25 - itemArmor.damageReduceAmount;
        float f3 = f2 * (float)n2 / 25.0f;
        int n3 = Math.min(20, EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic));
        if (n3 > 0) {
            int n4 = 25 - n3;
            f3 = f3 * (float)n4 / 25.0f;
        }
        return f3;
    }

    private static int y(ItemStack itemStack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
    }

    public static int d(Item item) {
        if (item instanceof bw) {
            return 0;
        }
        if (item instanceof ItemAxe) {
            return 1;
        }
        if (item instanceof cn) {
            return 2;
        }
        return 0;
    }

    public static ItemStack getCustomSkull(String string, String string2) {
        String string3 = String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", string2);
        String string4 = Base64.getEncoder().encodeToString(string3.getBytes());
        return ItemUtil.getItemStack(String.format("skull 1 3 {SkullOwner:{Id:\"%s\",Name:\"%s\",Properties:{textures:[{Value:\"%s\"}]}}}", UUID.randomUUID(), string, string4));
    }

    public static ItemStack getItemStack(String string) {
        try {
            String string2 = string.replace('&', '\u00a7');
            int n2 = 1;
            int n3 = 0;
            String[] stringArray = string2.split(" ");
            ResourceLocation resourceLocation = new ResourceLocation(stringArray[0]);
            Item item = Item.itemRegistry.getObject(resourceLocation);
            if (stringArray.length >= 2 && stringArray[1].matches("\\d+")) {
                n2 = Integer.parseInt(stringArray[1]);
            }
            if (stringArray.length >= 3 && stringArray[2].matches("\\d+")) {
                n3 = Integer.parseInt(stringArray[2]);
            }
            ItemStack itemStack = new ItemStack(item, n2, n3);
            if (stringArray.length >= 4) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 3; i < stringArray.length; ++i) {
                    stringBuilder.append(" ").append(stringArray[i]);
                }
                itemStack.setTagCompound(JsonToNBT.getTagFromJson(stringBuilder.toString()));
            }
            return itemStack;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ItemStack(Blocks.barrier);
        }
    }

    public String getCustomSkullNBT(String string, String string2) {
        String string3 = String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", string2);
        String string4 = Base64.getEncoder().encodeToString(string3.getBytes());
        return String.format("SkullOwner:{Id:\"%s\",Name:\"%s\",Properties:{textures:[{Value:\"%s\"}]}}", UUID.randomUUID(), string, string4);
    }
}
