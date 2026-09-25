package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.Event;
import lombok.Generated;

public final class BackendS2CEvent implements Event {
    private final rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket jF;

    @Generated
    public rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket dd() {
        return this.jF;
    }

    @Generated
    public BackendS2CEvent(rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket var1) {
        this.jF = var1;
    }
}
