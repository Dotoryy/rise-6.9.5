package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.m;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class BlockFlight extends Mode<Flight> {
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        SlotComponent.setSlot(SlotUtil.vx());
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.ay(3) && aEg.thePlayer.ae > 15) {
            if (Math.abs(MoveUtil.predictedMotion(0.42F) - aEg.thePlayer.motionY) < 1.0E-4) {
                var1x.setOnGround(true);
            } else {
                ChatUtil.b("Not Set");
                aEg.thePlayer.motionY = 0.42F;
            }

            aEg.thePlayer.motionY = 0.42F;
        }

        SlotComponent slotcomponent = this.d(SlotComponent.class);
        if (SlotComponent.getItemStack() != null) {
            slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemBlock && PlayerUtil.p(0.0, -1.0, 0.0) instanceof BlockAir) {
                PacketUtil.send(new m());
                aEg.playerController
                    .onPlayerRightClick(
                        aEg.thePlayer,
                        aEg.theWorld,
                        aEg.thePlayer.getCurrentEquippedItem(),
                        new BlockPos(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY) - 1.0, aEg.thePlayer.posZ),
                        EnumFacing.UP,
                        new Vec3(aEg.thePlayer.posX, Math.floor(aEg.thePlayer.posY) - 1.0, aEg.thePlayer.posZ)
                    );
            }
        }
    };

    public BlockFlight(String var1, Flight flight) {
        super(var1, flight);
    }
}
