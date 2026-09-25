package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class NewNCPNoSlow extends Mode<NoSlow> {
    private int disable;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        this.disable++;
        this.handleFood();
        this.handlePotion();
        this.handleSword();
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (Client.a.g().c(KillAura.class).jE == null) {
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
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.disable = 0;

    public NewNCPNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }

    @Override
    public void onEnable() {
    }

    private void handleFood() {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            this.sendPlacement();
        }
    }

    private void handlePotion() {
        if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            this.sendPlacement();
        }
    }

    private void handleSword() {
        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            this.sendPlacement();
        }
    }

    private void sendPlacement() {
        if (this.disable > 10 && !BadPacketsComponent.bad(false, true, true, false, false) && Client.a.g().c(KillAura.class).jE == null) {
            PacketUtil.send(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 5, null, 0.0F, 0.0F, 0.0F));
        }
    }
}
