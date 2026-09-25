package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.viaversion.viabackwards.protocol.v1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_18_2to1_19.packet.ServerboundPackets1_19;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
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

public class WatchdogPredictionNoSlow extends Mode<NoSlow> {
    boolean wasUsingItem;
    int usingTicks;
    public NumberValue maxPingSpoof = new NumberValue("Max Ping Spoof", this, 8, 0, 30, 1);
    public NumberValue whenToFinishEating = new NumberValue("When to finish eating", this, 30, 20, 36, 1);
    public final BooleanValue nonBlinkSpeedBypass = new BooleanValue("Non-Blink Speed Bypass", this, true);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.getCurrentEquippedItem() != null) {
            Item item = aEg.thePlayer.getCurrentEquippedItem().getItem();
            if (aEg.thePlayer.isUsingItem()) {
                if ((!(item instanceof ItemSword) || !this.getParent().sword.wo())
                    && (
                        item instanceof ItemFood && this.getParent().food.wo() && aEg.thePlayer.isEating()
                            || item instanceof ItemBow && this.getParent().bow.wo()
                            || aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion
                                && !ItemPotion.isSplash(aEg.thePlayer.getHeldItem().getMetadata())
                                && this.getParent().potion.wo()
                                && aEg.thePlayer.isEating()
                    )) {
                    this.usingTicks++;
                    if (this.usingTicks > this.maxPingSpoof.wo().intValue()) {
                        BlinkComponent.a(30000, true, false, false, false, true);
                    }
                }

                this.wasUsingItem = true;
            } else if (this.wasUsingItem) {
                this.usingTicks = 0;
                this.wasUsingItem = false;
                BlinkComponent.dispatch();
            }

            if (this.usingTicks > this.whenToFinishEating.wo().intValue()) {
                aEg.gameSettings.cgI.setPressed(false);
            }
        }
    };
    @EventLink
    public final Listener<SprintEvent> onSprint = var1x -> {
        if (aEg.thePlayer.isUsingItem() && aEg.thePlayer.moveForward > 0.0F && this.nonBlinkSpeedBypass.wo() && this.usingTicks <= this.maxPingSpoof.wo().intValue()) {
            aEg.thePlayer.setSprinting(true);
        }
    };
    @EventLink(value = 1)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if ((!KillAura.attacking || !this.e(KillAura.class).isEnabled())
            && (!aEg.thePlayer.onGround || aEg.thePlayer.cqL > 2)
            && !aEg.gameSettings.keyBindRight.isKeyDown()
            && !aEg.gameSettings.keyBindLeft.isKeyDown()
            && aEg.thePlayer.Zl > 5
            && aEg.thePlayer.isUsingItem()
            && (aEg.thePlayer.getHeldItem() == null || !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword))
            && (aEg.thePlayer.getHeldItem() == null || !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow))) {
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl + 45.0F, aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood && this.usingTicks > this.maxPingSpoof.wo().intValue()) {
            var1x.setCancelled();
        }

        if (this.getParent().potion.wo()
            && aEg.thePlayer.isUsingItem()
            && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion
            && this.usingTicks > this.maxPingSpoof.wo().intValue()) {
            var1x.setCancelled();
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            PacketUtil.send(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            PacketQueueComponent.dispatch();
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_19)) {
                UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
                PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_19.USE_ITEM, userconnection);
                packetwrapper.write(Types.VAR_INT, 0);
                packetwrapper.write(Types.VAR_INT, aEg.playerController.GZ());
                packetwrapper.sendToServer(Protocol1_19To1_18_2.class);
            } else {
                SlotComponent slotcomponent = this.d(SlotComponent.class);
                PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
            }

            var1x.setCancelled();
        }

        if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow && this.usingTicks > this.maxPingSpoof.wo().intValue()) {
            var1x.setCancelled();
        }
    };

    public WatchdogPredictionNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }

    @Override
    public void onEnable() {
    }
}
