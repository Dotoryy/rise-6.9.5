package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class IntaveNoSlow extends Mode<NoSlow> {
    boolean usingItem;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        if (aEg.thePlayer.getCurrentEquippedItem() != null) {
            Item item = aEg.thePlayer.getCurrentEquippedItem().getItem();
            if (aEg.thePlayer.isUsingItem()) {
                if (item instanceof ItemSword && this.getParent().sword.wo()) {
                    PacketQueueComponent.cR = true;
                    if (aEg.thePlayer.ticksExisted % 5 == 0) {
                        PacketUtil.send(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        PacketQueueComponent.dispatch();
                        aEg.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(aEg.thePlayer.getCurrentEquippedItem()));
                    }
                } else if (item instanceof ItemFood && this.getParent().food.wo() || item instanceof ItemBow && this.getParent().bow.wo()) {
                    PacketQueueComponent.cR = true;
                }

                this.usingItem = true;
            } else if (this.usingItem) {
                this.usingItem = false;
                PacketQueueComponent.cR = false;
            }
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            var1x.setCancelled();
        }

        if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            var1x.setCancelled();
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            var1x.setCancelled();
        }

        if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            var1x.setCancelled();
        }
    };

    public IntaveNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
