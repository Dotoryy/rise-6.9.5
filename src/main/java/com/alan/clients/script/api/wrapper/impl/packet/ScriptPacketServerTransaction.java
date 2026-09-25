package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class ScriptPacketServerTransaction extends ScriptPacket<S32PacketConfirmTransaction> {
    public ScriptPacketServerTransaction(S32PacketConfirmTransaction packet) {
        super(packet);
    }

    public int getWindowId() {
        return this.wrapped.getWindowId();
    }

    public short getActionNumber() {
        return this.wrapped.getActionNumber();
    }

    public boolean isAccepted() {
        return this.wrapped.func_148888_e();
    }
}
