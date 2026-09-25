package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public class LegitNoSlow extends Mode<NoSlow> {
    private static final int MAX_USE_TICKS = 32;
    private static final int MIN_USE_TICKS = 12;
    private static final int Ni = 0;
    private int useTicks;
    private boolean heardBurp;
    private boolean slowDownStarted;
    private boolean delaying;
    private int delayTicks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer != null && aEg.thePlayer.getHeldItem() != null) {
            if (PacketQueueComponent.cR) {
                this.useTicks++;
            }

            if (this.shouldStopUsing()) {
                if (!PacketQueueComponent.cR) {
                    PacketQueueComponent.cR = true;
                    this.useTicks = aEg.thePlayer.getItemInUseDuration();
                }

                aEg.thePlayer.stopUsingItem();
                aEg.gameSettings.cgI.setPressed(false);
            }

            if (PacketQueueComponent.cR && (this.useTicks >= 32 || this.heardBurp)) {
                this.flushQueue();
            }
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (!this.isUsingBypassedItem()) {
            this.resetDelay();
        } else {
            boolean flag = aEg.thePlayer.getItemInUseDuration() % 3 != 0;
            if (!flag) {
                this.resetDelay();
            } else if (!this.slowDownStarted) {
                this.delaying = true;
                this.delayTicks = 0;
                this.slowDownStarted = true;
            } else if (this.delaying) {
                this.delayTicks--;
                if (this.delayTicks <= 0) {
                    this.delaying = false;
                    aEg.thePlayer.setSprinting(true);
                }
            } else {
                aEg.thePlayer.setSprinting(true);
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect s29packetsoundeffect = (S29PacketSoundEffect)var1x.getPacket();
            if (s29packetsoundeffect.getSoundName() != null && s29packetsoundeffect.getSoundName().contains("random.burp")) {
                this.heardBurp = true;
            }
        }
    };

    public LegitNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }

    @Override
    public void onDisable() {
        this.flushQueue();
        this.resetDelay();
        aEg.gameSettings.cgI.setPressed(GameSettings.isKeyDown(aEg.gameSettings.cgI));
    }

    private boolean shouldStopUsing() {
        if (!aEg.thePlayer.isUsingItem()) {
            return false;
        }

        ItemStack itemstack = aEg.thePlayer.getHeldItem();
        if (itemstack == null) {
            return false;
        }

        Item item = itemstack.getItem();
        return aEg.thePlayer.getItemInUseDuration() > 12 && !(item instanceof ItemBow) && this.appliesTo(itemstack, false);
    }

    private boolean isUsingBypassedItem() {
        if (!aEg.thePlayer.isUsingItem()) {
            return false;
        }

        ItemStack itemstack = aEg.thePlayer.getHeldItem();
        return itemstack != null && this.appliesTo(itemstack, true);
    }

    private boolean appliesTo(ItemStack stack, boolean var2) {
        Item item = stack.getItem();
        if (item instanceof ItemFood) {
            return this.getParent().food.wo();
        } else if (item instanceof ItemPotion) {
            return this.getParent().potion.wo() && !ItemPotion.isSplash(stack.getMetadata());
        }
        return item instanceof ItemSword ? this.getParent().sword.wo() : var2 && item instanceof ItemBow && this.getParent().bow.wo();
    }

    private void flushQueue() {
        if (PacketQueueComponent.cR) {
            PacketQueueComponent.dispatch();
            PacketQueueComponent.cR = false;
        }

        this.heardBurp = false;
        this.useTicks = 0;
    }

    private void resetDelay() {
        this.slowDownStarted = false;
        this.delaying = false;
        this.delayTicks = 0;
    }
}
