package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

@ModuleInfo(aliases = "module.player.inventorysync.name", description = "module.player.inventorysync.description", category = Category.PLAYER)
public class InventorySync extends Module {
    public short action;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1 -> {
        if (var1.getPacket() instanceof S32PacketConfirmTransaction s32packetconfirmtransaction) {
            Container container = aEg.thePlayer.inventoryContainer;
            if (s32packetconfirmtransaction.getWindowId() == container.windowId) {
                this.action = s32packetconfirmtransaction.getActionNumber();
                if (this.action > 0 && this.action < container.transactionID) {
                    container.transactionID = (short)(this.action + 1);
                }
            }
        }
    };

    public InventorySync() {
    }
}
