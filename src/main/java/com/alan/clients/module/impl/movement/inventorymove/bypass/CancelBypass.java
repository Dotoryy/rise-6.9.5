package com.alan.clients.module.impl.movement.inventorymove.bypass;

import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.q;

public class CancelBypass extends Mode<InventoryMove> {
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
            var0.setCancelled();
        }

        if (packet instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)packet).getAction() == Action.OPEN_INVENTORY) {
            var0.setCancelled();
        }

        if (packet instanceof q) {
            var0.setCancelled();
        }
    };
    private final KeyBinding[] movementKeys = new KeyBinding[]{
        aEg.gameSettings.keyBindForward,
        aEg.gameSettings.keyBindBack,
        aEg.gameSettings.keyBindRight,
        aEg.gameSettings.keyBindLeft,
        aEg.gameSettings.keyBindJump
    };
    @EventLink
    public final Listener<SprintEvent> onSprint = var0 -> {};
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (!(aEg.currentScreen instanceof GuiChat) && aEg.currentScreen != this.getStandardClickGUI()) {
            for (KeyBinding keybinding : this.movementKeys) {
                keybinding.setPressed(GameSettings.isKeyDown(keybinding));
            }
        }
    };

    public CancelBypass(String var1, InventoryMove inventoryMove) {
        super(var1, inventoryMove);
    }
}
