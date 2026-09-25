package com.alan.clients.script.api.wrapper.impl.event.impl;

import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.script.api.wrapper.impl.event.CancellableScriptEvent;
import com.alan.clients.script.api.wrapper.impl.packet.ScriptPacket;
import com.alan.clients.script.api.wrapper.impl.packet.ScriptPacketFactory;

public class ScriptPacketReceiveEvent extends CancellableScriptEvent<PacketReceiveEvent> {
    private ScriptPacket<?> wrappedPacket;

    public ScriptPacketReceiveEvent(PacketReceiveEvent event) {
        super(event);
        this.wrappedPacket = ScriptPacketFactory.wrap(event.getPacket());
    }

    public String getPacketName() {
        return this.wrapped.getPacket().getClass().getSimpleName();
    }

    public String getPacketClassName() {
        return this.wrapped.getPacket().getClass().getName();
    }

    public boolean isPacket(String var1) {
        return this.wrappedPacket.isType(var1);
    }

    public ScriptPacket<?> getPacket() {
        return this.wrappedPacket;
    }

    @Override
    public String getHandlerName() {
        return "onPacketReceive";
    }
}
