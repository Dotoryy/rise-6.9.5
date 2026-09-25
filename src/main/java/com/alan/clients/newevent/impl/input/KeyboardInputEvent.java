package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Event;
import com.alan.clients.script.api.wrapper.impl.event.ScriptEvent;
import com.alan.clients.script.api.wrapper.impl.event.impl.ScriptKeyboardInputEvent;
import lombok.Generated;
import net.minecraft.client.gui.GuiScreen;

public final class KeyboardInputEvent extends CancellableEvent {
    private final int keyCode;
    private final char jm;
    private final GuiScreen guiScreen;

    @Override
    public ScriptEvent<? extends Event> getScriptEvent() {
        return new ScriptKeyboardInputEvent(this);
    }

    @Generated
    public int getKeyCode() {
        return this.keyCode;
    }

    @Generated
    public char cP() {
        return this.jm;
    }

    @Generated
    public GuiScreen getGuiScreen() {
        return this.guiScreen;
    }

    @Generated
    public KeyboardInputEvent(int keyCode, char var2, GuiScreen guiScreen) {
        this.keyCode = keyCode;
        this.jm = var2;
        this.guiScreen = guiScreen;
    }
}
