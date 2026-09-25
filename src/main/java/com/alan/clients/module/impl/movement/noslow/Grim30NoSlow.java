package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.newevent.impl.input.RightClickEvent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class Grim30NoSlow extends Mode<NoSlow> {
    @EventLink
    public Listener<SprintEvent> onSprint;
    public BooleanValue heypixel = new BooleanValue("Heypixel", this, false);
    @EventLink
    public Listener<RightClickEvent> onRightClick;
    public int usingItemTicks;
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    @EventLink
    public Listener<PreUpdateEvent> onPreUpdate;
    @EventLink
    public Listener<MoveInputEvent> onMoveInput;
    @EventLink
    public Listener<BlockAABBEvent> onBlockAABB = var0 -> {};
    @EventLink
    public Listener<SlowDownEvent> onSlowDown;

    public Grim30NoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
        this.onPreUpdate = var1x -> {
            if (aEg.thePlayer.isUsingItem()
                && !aEg.thePlayer.onGround
                && !aEg.gameSettings.keyBindRight.isKeyDown()
                && !aEg.gameSettings.keyBindLeft.isKeyDown()) {
                RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl + 45.0F, aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
            }

            if (aEg.thePlayer.isInWeb) {
                MoveUtil.strafe(0.64);
            }

            if (aEg.thePlayer.isUsingItem() && aEg.thePlayer.cqL > 1 && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                if (!this.e(Speed.class).isEnabled()) {
                    MoveUtil.moveFlying(2.0E-4);
                } else {
                    MoveUtil.moveFlying(1.0E-4);
                }

                if (!aEg.gameSettings.keyBindRight.isKeyDown()
                    && !aEg.gameSettings.keyBindLeft.isKeyDown()
                    && !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
                    RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl + 45.0F, aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
                }
            }
        };
        this.onPacketSend = var1x -> {
            if (this.heypixel.wo() && var1x.dq() instanceof C0FPacketConfirmTransaction) {
                if (aEg.thePlayer.isUsingItem()
                    && (
                        aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood
                            || aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion
                            || aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow
                    )) {
                    var1x.setCancelled();
                }
            }
        };
        this.onMoveInput = var1x -> {
            if (this.usingItemTicks >= 20) {
                ;
            }
        };
        this.onSprint = var0 -> {
            if (aEg.thePlayer.isUsingItem() && aEg.thePlayer.moveForward > 0.0F) {
                aEg.thePlayer.setSprinting(true);
            }
        };
        this.onRightClick = var0 -> {
            if (aEg.thePlayer.tR % 2 == 1 && !aEg.thePlayer.onGround) {
                var0.setCancelled();
            }
        };
        this.onSlowDown = var1x -> {
            if (aEg.thePlayer.isUsingItem()) {
                this.usingItemTicks++;
                if (!this.e(Speed.class).isEnabled()) {
                    MoveUtil.moveFlying(1.0E-4);
                }
            } else {
                this.usingItemTicks = 0;
            }

            if (aEg.thePlayer.cqL == 1 || aEg.thePlayer.tR % 2 == 0 && !aEg.thePlayer.onGround || aEg.thePlayer.cqL % 2 == 1 && aEg.thePlayer.onGround) {
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
    }


    static {
    }
}
