package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class BlinkNoSlow extends Mode<NoSlow> {
    private boolean enabled;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.thePlayer.isUsingItem()) {
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemFood) {
                BlinkComponent.blink();
                if (aEg.thePlayer.itemInUseCount < -1) {
                    aEg.gameSettings.cgI.setPressed(false);
                }

                return;
            }
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        Packet packet = var1x.dq();
        if (packet instanceof C07PacketPlayerDigging) {
            ChatUtil.b("digging " + ((C07PacketPlayerDigging)packet).getStatus() + " " + var1x.isCancelled());
            this.enabled = false;
        } else if (!this.enabled && packet instanceof C08PacketPlayerBlockPlacement) {
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack() != null) {
                slotcomponent = this.d(SlotComponent.class);
                if (SlotComponent.getItemStack().getItem() instanceof ItemFood) {
                    this.enabled = true;
                    BlinkComponent.blink();
                    BlinkComponent.enabled = true;
                    var1x.setCancelled();
                    PacketUtil.sendNoEvent(packet);
                    ChatUtil.b("Started");
                }
            }
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        if (SlotComponent.getItemStack() != null) {
            slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemFood) {
                var1x.setCancelled();
            }
        }
    };

    public BlinkNoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
