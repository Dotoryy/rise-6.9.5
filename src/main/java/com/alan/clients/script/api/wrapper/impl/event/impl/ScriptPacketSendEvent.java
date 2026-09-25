package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;
import com.alan.clients.script.api.wrapper.impl.packet.ScriptPacket;
import com.alan.clients.script.api.wrapper.impl.packet.ScriptPacketFactory;

public class ScriptPacketSendEvent extends CancellableScriptEvent<PacketSendEvent> {
    private ScriptPacket<?> wrappedPacket;

    public ScriptPacketSendEvent(PacketSendEvent event) {
        super(event);
        this.wrappedPacket = ScriptPacketFactory.wrap(event.dq());
    }

    public String getPacketName() {
        return this.wrapped.dq().getClass().getSimpleName();
    }

    public String getPacketClassName() {
        return this.wrapped.dq().getClass().getName();
    }

    public boolean isPacket(String var1) {
        return this.wrappedPacket.isType(var1);
    }

    public ScriptPacket<?> getPacket() {
        return this.wrappedPacket;
    }

    @Override
    public String getHandlerName() {
        return "onPacketSend";
    }
}
