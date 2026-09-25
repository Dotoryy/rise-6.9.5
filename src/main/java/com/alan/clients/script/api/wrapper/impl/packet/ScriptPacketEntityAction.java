package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.client.C0BPacketEntityAction;

public class ScriptPacketEntityAction extends ScriptPacket<C0BPacketEntityAction> {
    public ScriptPacketEntityAction(C0BPacketEntityAction packet) {
        super(packet);
    }

    public String getAction() {
        return this.wrapped.getAction().name();
    }

    public int getAuxData() {
        return this.wrapped.getAuxData();
    }
}
