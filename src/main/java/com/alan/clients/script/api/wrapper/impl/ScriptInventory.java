package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.wrapper.ScriptWrapper;
import net.minecraft.entity.player.InventoryPlayer;

public class ScriptInventory extends ScriptWrapper<InventoryPlayer> {
    public ScriptInventory(InventoryPlayer inventory) {
        super(inventory);
    }

    public ScriptItemStack getItemStackInSlot(int var1) {
        return new ScriptItemStack(this.wrapped.getStackInSlot(var1));
    }

    private int slot(int var1) {
        if (var1 >= 36) {
            return 8 - (var1 - 36);
        }
        return var1 < 9 ? var1 + 36 : var1;
    }

    public ScriptItemStack getHeldItem() {
        return new ScriptItemStack(this.wrapped.getCurrentItem());
    }
}
