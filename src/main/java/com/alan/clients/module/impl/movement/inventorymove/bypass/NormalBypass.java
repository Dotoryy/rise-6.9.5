package com.alan.clients.module.impl.movement.inventorymove.bypass;

import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.Mode;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public final class NormalBypass extends Mode<InventoryMove> {
    private final KeyBinding[] movementKeys = new KeyBinding[]{
        aEg.gameSettings.keyBindForward,
        aEg.gameSettings.keyBindBack,
        aEg.gameSettings.keyBindRight,
        aEg.gameSettings.keyBindLeft,
        aEg.gameSettings.keyBindJump
    };
    @EventLink
    private final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.currentScreen != null && !(aEg.currentScreen instanceof GuiChat) && aEg.currentScreen != this.getStandardClickGUI()) {
            for (KeyBinding keybinding : this.movementKeys) {
                keybinding.setPressed(GameSettings.isKeyDown(keybinding));
            }
        }
    };

    public NormalBypass(String var1, InventoryMove inventoryMove) {
        super(var1, inventoryMove);
    }
}
