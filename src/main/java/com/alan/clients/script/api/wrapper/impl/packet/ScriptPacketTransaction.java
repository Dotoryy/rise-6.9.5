package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class ScriptPacketTransaction extends ScriptPacket<C0FPacketConfirmTransaction> {
    public ScriptPacketTransaction(C0FPacketConfirmTransaction packet) {
        super(packet);
    }

    public int getWindowId() {
        return this.wrapped.getWindowId();
    }

    public short getUid() {
        return this.wrapped.getUid();
    }

    public void setWindowId(int windowId) {
        this.wrapped.setWindowId(windowId);
    }

    public void setUid(short var1) {
        this.wrapped.setUid(var1);
    }

    public void setAccepted(boolean accepted) {
        this.wrapped.setAccepted(accepted);
    }
}
