package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.play.server.S42PacketCombatEvent;

public class ScriptPacketCombatEvent extends ScriptPacket<S42PacketCombatEvent> {
    public ScriptPacketCombatEvent(S42PacketCombatEvent event) {
        super(event);
    }

    public String getEventType() {
        return this.wrapped.eventType != null ? this.wrapped.eventType.name() : "";
    }

    public int getPlayerId() {
        return this.wrapped.field_179774_b;
    }

    public int getEntityId() {
        return this.wrapped.field_179775_c;
    }
}
