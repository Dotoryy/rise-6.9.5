package com.alan.clients.script.api.wrapper.impl;

import com.alan.clients.script.api.wrapper.ScriptWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScriptItemStack extends ScriptWrapper<ItemStack> {
    public ScriptItemStack(ItemStack stack) {
        super(stack);
    }

    public int getAmount() {
        return this.wrapped.jI();
    }

    public int getMaxAmount() {
        return this.wrapped.getMaxStackSize();
    }

    public int getItemId() {
        return this.wrapped == null ? 0 : Item.getIdFromItem(this.wrapped.getItem());
    }

    public ItemStack getWrapped() {
        return this.wrapped;
    }

    public String getName() {
        return this.wrapped.getDisplayName();
    }
}
