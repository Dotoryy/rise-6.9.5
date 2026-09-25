package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.newevent.impl.input.RightClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.l;

public class   WatchdogNoSlow extends Mode<NoSlow> {
    private int airTicks;
    private boolean dk;
    private boolean onSlab;
    private Packet<?> NI;
    private KillAura killAura = null;
    public final BooleanValue slowDownOnSlabs = new BooleanValue("Slow down on Slabs", this, true);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air && !aEg.thePlayer.isUsingItem() && this.slowDownOnSlabs.wo()) {
            this.onSlab = false;
        }

        double d0 = var1x.getPosY();
        if (Math.abs(d0 - Math.round(d0)) > 0.03 && aEg.thePlayer.onGround) {
            this.onSlab = true;
        }

        if (aEg.thePlayer.isUsingItem() && !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            if (aEg.thePlayer.onGround) {
                this.airTicks = 0;
            } else {
                this.airTicks++;
            }

            if (this.airTicks >= 2) {
                this.dk = false;
                this.NI = null;
            } else if (aEg.thePlayer.onGround && !this.onSlab) {
                var1x.setPosY(var1x.getPosY() + 0.001);
            }
        }

        if (this.onSlab && !aEg.thePlayer.onGround && aEg.thePlayer.isUsingItem() && !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            aEg.thePlayer.motionX *= 0.1;
            aEg.thePlayer.motionZ *= 0.1;
        }
    };
    @EventLink
    public final Listener<RightClickEvent> onRightClick = var1x -> {
        if (aEg.thePlayer.getHeldItem() != null) {
            if (aEg.thePlayer.isUsingItem()
                || aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion && !ItemPotion.isSplash(aEg.thePlayer.getHeldItem().getMetadata())
                || aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood
                || aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                if (aEg.thePlayer.tR < 2 && aEg.thePlayer.tR != 0 && !this.onSlab) {
                    ChatUtil.b("You must start eating while in the air even with potions");
                    var1x.setCancelled();
                } else if (aEg.thePlayer.onGround) {
                    aEg.thePlayer.jump();
                    var1x.setCancelled();
                }
            }
        }
    };
    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (this.killAura == null) {
            this.killAura = this.e(KillAura.class);
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (!this.onSlab || aEg.thePlayer.onGround) {
            if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                var1x.setCancelled();
            }

            if (this.getParent().potion.wo()
                && aEg.thePlayer.isUsingItem()
                && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion
                && !ItemPotion.isSplash(aEg.thePlayer.getHeldItem().getMetadata())) {
                var1x.setCancelled();
            }

            if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                var1x.setCancelled();
            }
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            PacketUtil.send(new l(SlotComponent.bQ() % 7 + (int)(Math.random() * 2.0) + 1));
            slotcomponent = this.d(SlotComponent.class);
            PacketUtil.send(new l(SlotComponent.bQ()));
            var1x.setCancelled();
        }
    };

    public WatchdogNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
