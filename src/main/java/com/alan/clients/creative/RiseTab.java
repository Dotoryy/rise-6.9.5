package com.alan.clients.creative;

import com.alan.clients.Client;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.player.ItemUtil;
import com.alan.clients.util.player.PlayerUtil;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;

public final class RiseTab extends CreativeTabs implements InstanceAccess {
    public RiseTab() {
        super(12, "rise");
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> stacks) {
        ItemStack itemstack = new ItemStack(Items.armor_stand);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagDouble(aEg.thePlayer.posX));
        nbttaglist.appendTag(new NBTTagDouble(aEg.thePlayer.posY));
        nbttaglist.appendTag(new NBTTagDouble(aEg.thePlayer.posZ));
        nbttagcompound.setBoolean("Invisible", true);
        nbttagcompound.setBoolean("NoGravity", true);
        nbttagcompound.setBoolean("CustomNameVisible", true);
        nbttagcompound.setString("CustomName", "Rise Client");
        nbttagcompound.setTag("Pos", nbttaglist);
        nbttagcompound.setTag("pose", nbttaglist);
        itemstack.setTagInfo("EntityTag", nbttagcompound);
        itemstack.setStackDisplayName("§rHologram");
        stacks.add(itemstack);
        ItemStack itemstack1 = new ItemStack(Items.armor_stand);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        NBTTagList nbttaglist1 = new NBTTagList();
        nbttaglist1.appendTag(new NBTTagDouble(aEg.thePlayer.posX));
        nbttaglist1.appendTag(new NBTTagDouble(aEg.thePlayer.posY));
        nbttaglist1.appendTag(new NBTTagDouble(aEg.thePlayer.posZ));
        nbttagcompound1.setBoolean("Invisible", true);
        nbttagcompound1.setBoolean("NoGravity", true);
        nbttagcompound1.setBoolean("CustomNameVisible", true);
        nbttagcompound1.setString("CustomName", "\"Rise Client\"");
        nbttagcompound1.setTag("Pos", nbttaglist1);
        itemstack1.setTagInfo("EntityTag", nbttagcompound1);
        itemstack1.setStackDisplayName("§rHologram (Via Version)");
        stacks.add(itemstack1);
        stacks.add(ItemUtil.getCustomSkull("Suspicious", "https://education.minecraft.net/wp-content/uploads/server.jpg"));
        stacks.add(ItemUtil.getCustomSkull("Hentai", "https://education.minecraft.net/wp-content/uploads/cock.png"));
        ItemStack itemstack2 = ItemUtil.getItemStack("anvil 1 100");
        itemstack2.setStackDisplayName("§rSpawn Imposter");
        stacks.add(itemstack2);
        ItemStack itemstack3 = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:6,Amplifier:125,Duration:1000000}]}");
        itemstack3.setStackDisplayName("§rSplash Potion of Instant Death");
        stacks.add(itemstack3);
        stacks.add(ItemUtil.getItemStack("dragon_egg"));
        stacks.add(ItemUtil.getItemStack("barrier"));
        stacks.add(ItemUtil.getItemStack("command_block"));
        stacks.add(ItemUtil.getItemStack("command_block_minecart"));
        ItemStack itemstack4 = ItemUtil.getItemStack("stone_slab 1 2");
        itemstack4.setStackDisplayName("§rAlpha Slab");
        stacks.add(itemstack4);
        ItemStack itemstack5 = ItemUtil.getItemStack("leaves 1 4");
        itemstack5.setStackDisplayName("§rAlpha Leaves");
        stacks.add(itemstack5);
        stacks.add(ItemUtil.getItemStack("tallgrass 1 0"));
        ItemStack itemstack6 = ItemUtil.getItemStack(
            "potion 1 16385 {CustomPotionEffects:[{Id:15,Amplifier:2,Duration:1000000},{Id:2,Amplifier:2,Duration:1000000},{Id:9,Amplifier:2,Duration:1000000},{Id:19,Amplifier:2,Duration:1000000},{Id:20,Amplifier:2,Duration:1000000},{Id:18,Amplifier:2,Duration:1000000},{Id:17,Amplifier:2,Duration:1000000},{Id:14,Amplifier:2,Duration:1000000},{Id:4,Amplifier:2,Duration:1000000}]}"
        );
        itemstack6.setStackDisplayName("§rSplash Potion of Annoyance");
        stacks.add(itemstack6);
        ItemStack itemstack7 = ItemUtil.getItemStack("potion 1 16385 {CustomPotionEffects:[{Id:14,Duration:1000000,ShowParticles:0b}]}");
        itemstack7.setStackDisplayName("§rSplash Potion of Infinite Invisibility");
        stacks.add(itemstack7);
        ItemStack itemstack8 = ItemUtil.getItemStack(
            "diamond_sword 1 0 {ench:[{id:19,lvl:32767},{id:20,lvl:32767},{id:18,lvl:32767},{id:16,lvl:32767},{id:17,lvl:32767}],Unbreakable:1}"
        );
        itemstack8.setStackDisplayName("§r§b§lGod Sword");
        stacks.add(itemstack8);
        ItemStack itemstack9 = ItemUtil.getItemStack(
            "bow 1 0 {ench:[{id:48,lvl:32767},{id:49,lvl:32767},{id:50,lvl:32767},{id:51,lvl:32767},{id:19,lvl:32767}],Unbreakable:1}"
        );
        itemstack9.setStackDisplayName("§r§b§lGod Bow");
        stacks.add(itemstack9);
        ItemStack itemstack10 = ItemUtil.getItemStack(
            "diamond_helmet 1 0 {ench:[{id:0,lvl:32767},{id:6,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767},{id:4,lvl:32767}],Unbreakable:1}"
        );
        itemstack10.setStackDisplayName("§r§b§lGod Helmet");
        stacks.add(itemstack10);
        ItemStack itemstack11 = ItemUtil.getItemStack("diamond_chestplate 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        itemstack11.setStackDisplayName("§r§b§lGod Chestplate");
        stacks.add(itemstack11);
        ItemStack itemstack12 = ItemUtil.getItemStack("diamond_leggings 1 0  {ench:[{id:0,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}");
        itemstack12.setStackDisplayName("§r§b§lGod Leggings");
        stacks.add(itemstack12);
        ItemStack itemstack13 = ItemUtil.getItemStack(
            "diamond_boots 1 0  {ench:[{id:0,lvl:32767},{id:8,lvl:32767},{id:3,lvl:32767},{id:1,lvl:32767},{id:7,lvl:32767}],Unbreakable:1}"
        );
        itemstack13.setStackDisplayName("§r§b§lGod Boots");
        stacks.add(itemstack13);
        ItemStack itemstack14 = ItemUtil.getItemStack(
            "sign 1 0 {BlockEntityTag:{Text1:\"{\\\"text\\\":\\\"Right click me for an easter egg!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op "
                + PlayerUtil.name()
                + "\\\"}}\"}}"
        );
        itemstack14.setStackDisplayName("§rOP Sign");
        stacks.add(itemstack14);
        ItemStack itemstack15 = ItemUtil.getItemStack(
            "written_book 1 0 {pages:[\"{\\\"text\\\":\\\"Click me for an Easter Egg!\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/op "
                + PlayerUtil.name()
                + "\\\"}}\"],title:\"Easter Egg\",author:"
                + PlayerUtil.name()
                + "}"
        );
        stacks.add(itemstack15);
    }

    @Override
    public String getTranslatedTabLabel() {
        return Client.b;
    }

    @Override
    public Item getTabIconItem() {
        return Items.diamond;
    }
}
