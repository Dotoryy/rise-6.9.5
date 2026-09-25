package com.alan.clients.module.impl.movement.inventorymove;

import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public final class GrimInventoryMove extends Mode<InventoryMove> {
    private final KeyBinding[] movementKeys = new KeyBinding[]{
        aEg.gameSettings.keyBindForward,
        aEg.gameSettings.keyBindBack,
        aEg.gameSettings.keyBindRight,
        aEg.gameSettings.keyBindLeft,
        aEg.gameSettings.keyBindJump
    };
    private final NumberValue managerExtraSprintTicks = new NumberValue("Manager Extra Sprint Ticks", this, 9, 0, 20, 1);
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.currentScreen != null && !(aEg.currentScreen instanceof GuiChat) && aEg.currentScreen != this.getStandardClickGUI()) {
            for (KeyBinding keybinding : this.movementKeys) {
                keybinding.setPressed(GameSettings.isKeyDown(keybinding));
            }
        }
    };
    @EventLink
    public final Listener<SprintEvent> onSprint = var0 -> {
        if (aEg.currentScreen instanceof GuiInventory || aEg.currentScreen instanceof GuiChest) {
            aEg.thePlayer.setSprinting(false);
        }
    };

    public GrimInventoryMove(String var1, InventoryMove inventoryMove) {
        super(var1, inventoryMove);
    }

    public int getManagerExtraSprintTicks() {
        return this.managerExtraSprintTicks.wo().intValue();
    }
}
