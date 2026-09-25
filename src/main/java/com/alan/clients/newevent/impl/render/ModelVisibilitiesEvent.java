package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;
import lombok.Generated;
import net.minecraft.item.ItemStack;

public final class ModelVisibilitiesEvent implements Event {
    private final ItemStack kb;
    private int kc;

    @Generated
    public ItemStack bO() {
        return this.kb;
    }

    @Generated
    public int dz() {
        return this.kc;
    }

    @Generated
    public void l(int var1) {
        this.kc = var1;
    }

    @Generated
    public ModelVisibilitiesEvent(ItemStack stack, int var2) {
        this.kb = stack;
        this.kc = var2;
    }
}
