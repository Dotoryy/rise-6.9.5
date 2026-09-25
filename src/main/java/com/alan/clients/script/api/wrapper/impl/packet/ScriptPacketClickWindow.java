package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C0EPacketClickWindow;

public class ScriptPacketClickWindow extends ScriptPacket<C0EPacketClickWindow> {
    public ScriptPacketClickWindow(C0EPacketClickWindow packet) {
        super(packet);
    }

    public int getWindowId() {
        return this.wrapped.getWindowId();
    }

    public int getSlotId() {
        return this.wrapped.getSlotId();
    }

    public int getUsedButton() {
        return this.wrapped.getUsedButton();
    }

    public short getActionNumber() {
        return this.wrapped.getActionNumber();
    }

    public int getMode() {
        return this.wrapped.getMode();
    }
}
