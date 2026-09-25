package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.PlayerUtil;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.e;

@ModuleInfo(aliases = "module.other.murdermystery.name", description = "module.other.murdermystery.description", category = Category.PLAYER)
public final class MurderMystery extends Module {
    private final BooleanValue newestMethod = new BooleanValue("Newest Method", this, true);
    private final BooleanValue callOut = new BooleanValue("Call Out", this, false);
    private EntityPlayer murderer;
    private final Set<Integer> announced = new HashSet<>();
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (var1.getPacket() instanceof e e && e.getEquipmentSlot() == 0 && e.getItemStack() != null && this.isMeleeWeapon(e.getItemStack()) && !this.announced.contains(e.getEntityID())
            )
         {
            Entity entity = aEg.theWorld.getEntityByID(e.getEntityID());
            if (entity instanceof EntityPlayer) {
                ChatUtil.b(entity.getName() + " is The Murderer.");
                this.announced.add(e.getEntityID());
                this.murderer = (EntityPlayer)entity;
                if (this.callOut.wo()) {
                    aEg.thePlayer.sendChatMessage(entity.getName() + " is The Murderer.");
                }
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.thePlayer.ticksExisted % 2 != 0 && this.murderer == null && !this.newestMethod.wo()) {
            for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
                if (entityplayer.getHeldItem() != null && entityplayer.getHeldItem().getDisplayName().contains("Knife")) {
                    ChatUtil.b(PlayerUtil.g(entityplayer) + " is The Murderer.");
                    this.murderer = entityplayer;
                }
            }
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.murderer = null;

    public MurderMystery() {
    }

    private boolean isMeleeWeapon(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("ExtraAttributes", 10)
            ? stack.getTagCompound().getCompoundTag("ExtraAttributes").hasKey("MELEE", 1)
            : false;
    }
}
