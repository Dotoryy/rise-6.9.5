package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;
import net.minecraft.client.gui.GuiScreen;

public final class GuiKeyEvent extends CancellableEvent {
    private final int je;
    private final char jf;
    private final GuiScreen jg;

    @Generated
    public int cO() {
        return this.je;
    }

    @Generated
    public char cP() {
        return this.jf;
    }

    @Generated
    public GuiScreen cQ() {
        return this.jg;
    }

    @Generated
    public GuiKeyEvent(int var1, char var2, GuiScreen screen) {
        this.je = var1;
        this.jf = var2;
        this.jg = screen;
    }
}
