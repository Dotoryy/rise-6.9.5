package com.alan.clients.module.impl.movement;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ModuleInfo(aliases = "module.movement.noclip.name", description = "module.movement.noclip.description", category = Category.MOVEMENT)
public class NoClip extends Module {
    private final BooleanValue block = new BooleanValue("Block", this, false);
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (PlayerUtil.vk()) {
            var0.setBoundingBox(null);
            if (!(var0.getBlock() instanceof BlockAir) && !aEg.gameSettings.keyBindSneak.isKeyDown()) {
                double d0 = var0.getBlockPos().getX();
                double d1 = var0.getBlockPos().getY();
                double d2 = var0.getBlockPos().getZ();
                if (d1 < aEg.thePlayer.posY) {
                    var0.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
                }
            }
        }
    };
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = CancellableEvent::setCancelled;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        aEg.thePlayer.noClip = true;
        if (this.block.wo()) {
            int i = SlotUtil.vx();
            if (i == -1 || PlayerUtil.vk()) {
                return;
            }

            SlotComponent slotcomponent = this.d(SlotComponent.class);
            SlotComponent.setSlot(i);
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 2.0 + Math.random(), MovementFix.NORMAL);
            if (RotationComponent.fk.y >= 89.0F
                && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK
                && aEg.thePlayer.posY == aEg.objectMouseOver.getBlockPos().up().getY()) {
                PlayerControllerMP playercontrollermp = aEg.playerController;
                EntityPlayerSP entityplayersp = aEg.thePlayer;
                WorldClient worldclient = aEg.theWorld;
                SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                playercontrollermp.onPlayerRightClick(
                    entityplayersp,
                    worldclient,
                    SlotComponent.getItemStack(),
                    aEg.objectMouseOver.getBlockPos(),
                    aEg.objectMouseOver.sideHit,
                    aEg.objectMouseOver.hitVec
                );
                aEg.thePlayer.swingItem();
            }
        }
    };

    public NoClip() {
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.noClip = false;
    }
}
