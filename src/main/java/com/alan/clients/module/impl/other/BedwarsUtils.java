package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;

@ModuleInfo(aliases = "module.other.bwutils.name", description = "module.other.bwutils.description", category = Category.PLAYER)
public final class  BedwarsUtils extends Module {
    private final Collection<EntityPlayer> ironSwordPlayers = new HashSet<>();
    private final Collection<EntityPlayer> diamondSwordPlayers = new HashSet<>();
    private final Collection<EntityPlayer> stoneSwordPlayers = new HashSet<>();
    private final Collection<EntityPlayer> diamondArmorPlayers = new HashSet<>();
    private final Collection<EntityPlayer> chainArmorPlayers = new HashSet<>();
    private final Collection<EntityPlayer> ironArmorPlayers = new HashSet<>();
    private final Collection<EntityPlayer> invisiblePlayers = new HashSet<>();
    private final BooleanValue swordReveal = new BooleanValue("Sword Reveal", this, true);
    private final BooleanValue includeStone = new BooleanValue("Include Stone", this, false, () -> !this.swordReveal.wo());
    private final BooleanValue armorReveal = new BooleanValue("Armor Reveal", this, true);
    private final BooleanValue invisibleCheck = new BooleanValue("Invisible Check", this, true);
    private final BooleanValue invisibilityStatus = new BooleanValue("Invisibility Status", this, false);
    private boolean selfInvisible = false;
    @EventLink
    private final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
            if (aEg.thePlayer == null && aEg.theWorld == null) {
                this.diamondSwordPlayers.clear();
                this.ironSwordPlayers.clear();
                this.stoneSwordPlayers.clear();
                this.diamondArmorPlayers.clear();
                this.ironArmorPlayers.clear();
                this.chainArmorPlayers.clear();
                this.invisiblePlayers.clear();
            } else {
                if (entityplayer.getHeldItem() != null) {
                    Item item = entityplayer.getHeldItem().getItem();
                    if (this.swordReveal.wo() && item instanceof ItemSword) {
                        String s = ((ItemSword)item).getToolMaterialName().toLowerCase();
                        if (s.contains("iron") && !this.ironSwordPlayers.contains(entityplayer)) {
                            this.ironSwordPlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " has an "
                                    + EnumChatFormatting.AQUA
                                    + "Iron Sword"
                            );
                        }

                        if (s.contains("emerald") && !this.diamondSwordPlayers.contains(entityplayer)) {
                            this.diamondSwordPlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " has a "
                                    + EnumChatFormatting.AQUA
                                    + "Diamond Sword"
                            );
                        }

                        if (s.contains("stone") && !this.stoneSwordPlayers.contains(entityplayer)) {
                            this.stoneSwordPlayers.add(entityplayer);
                            if (this.includeStone.wo()) {
                                ChatUtil.b(
                                    "Player "
                                        + EnumChatFormatting.RED
                                        + entityplayer.getName()
                                        + EnumChatFormatting.WHITE
                                        + " has a "
                                        + EnumChatFormatting.AQUA
                                        + "Stone Sword"
                                );
                            }
                        }

                        if (s.contains("wood")) {
                            this.stoneSwordPlayers.remove(entityplayer);
                            this.ironSwordPlayers.remove(entityplayer);
                            this.diamondSwordPlayers.remove(entityplayer);
                        }
                    }
                }

                if (this.armorReveal.wo()) {
                    ItemStack itemstack = entityplayer.getCurrentArmor(1);
                    if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                        if (((ItemArmor)itemstack.getItem()).getArmorMaterial().equals(ArmorMaterial.CHAIN) && !this.chainArmorPlayers.contains(entityplayer)) {
                            this.chainArmorPlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " has "
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + "Chain Armor"
                            );
                        }

                        if (((ItemArmor)itemstack.getItem()).getArmorMaterial().equals(ArmorMaterial.IRON) && !this.ironArmorPlayers.contains(entityplayer)) {
                            this.ironArmorPlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " has "
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + "Iron Armor"
                            );
                        }

                        if (((ItemArmor)itemstack.getItem()).getArmorMaterial().equals(ArmorMaterial.DIAMOND) && !this.diamondArmorPlayers.contains(entityplayer)) {
                            this.diamondArmorPlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " has "
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + "Diamond Armor"
                            );
                        }

                        if (((ItemArmor)itemstack.getItem()).getArmorMaterial().equals(ArmorMaterial.LEATHER)) {
                            this.diamondArmorPlayers.remove(entityplayer);
                            this.ironArmorPlayers.remove(entityplayer);
                            this.chainArmorPlayers.remove(entityplayer);
                        }
                    }
                }

                if (this.invisibleCheck.wo()) {
                    if (entityplayer.getActivePotionEffect(Potion.invisibility) != null) {
                        if (!this.invisiblePlayers.contains(entityplayer)) {
                            this.invisiblePlayers.add(entityplayer);
                            ChatUtil.b(
                                "Player "
                                    + EnumChatFormatting.RED
                                    + entityplayer.getName()
                                    + EnumChatFormatting.WHITE
                                    + " is now "
                                    + EnumChatFormatting.GOLD
                                    + "Invisible"
                            );
                        }
                    } else if (this.invisiblePlayers.contains(entityplayer)) {
                        this.invisiblePlayers.remove(entityplayer);
                        ChatUtil.b(
                            "Player "
                                + EnumChatFormatting.RED
                                + entityplayer.getName()
                                + EnumChatFormatting.WHITE
                                + " is now "
                                + EnumChatFormatting.GOLD
                                + "Visible"
                        );
                    }
                }

                if (this.invisibilityStatus.wo()) {
                    if (aEg.thePlayer.getActivePotionEffect(Potion.invisibility) != null) {
                        this.selfInvisible = true;
                        if (aEg.thePlayer.ticksExisted % 200 == 0) {
                            ChatUtil.b(
                                "Your Invisibility"
                                    + EnumChatFormatting.RED
                                    + " expires "
                                    + EnumChatFormatting.RESET
                                    + "in "
                                    + EnumChatFormatting.RED
                                    + aEg.thePlayer.getActivePotionEffect(Potion.invisibility).getDuration() / 20
                                    + EnumChatFormatting.RESET
                                    + " second(s)"
                            );
                        }
                    }
                } else if (this.selfInvisible) {
                    ChatUtil.b("Invisibility" + EnumChatFormatting.RED + " Expired");
                    this.selfInvisible = false;
                }
            }
        }
    };
    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = var1 -> {
        this.diamondSwordPlayers.clear();
        this.ironSwordPlayers.clear();
        this.stoneSwordPlayers.clear();
        this.chainArmorPlayers.clear();
        this.ironArmorPlayers.clear();
        this.diamondArmorPlayers.clear();
        this.invisiblePlayers.clear();
    };

    public BedwarsUtils() {
    }
}
