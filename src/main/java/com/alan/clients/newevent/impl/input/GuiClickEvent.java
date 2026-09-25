package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.CancellableEvent;
import lombok.Generated;

public final class GuiClickEvent extends CancellableEvent {
    private final int mouseX;
    private final int mouseY;
    private final int jd;

    @Generated
    public int getMouseX() {
        return this.mouseX;
    }

    @Generated
    public int getMouseY() {
        return this.mouseY;
    }

    @Generated
    public int cN() {
        return this.jd;
    }

    @Generated
    public GuiClickEvent(int var1, int var2, int var3) {
        this.mouseX = var1;
        this.mouseY = var2;
        this.jd = var3;
    }
}
