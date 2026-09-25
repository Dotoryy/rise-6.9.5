package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.Event;
import lombok.Generated;

public final class ServerJoinEvent implements Event {
    public String ip;
    public int port;

    @Generated
    public String getIp() {
        return this.ip;
    }

    @Generated
    public int getPort() {
        return this.port;
    }

    @Generated
    public ServerJoinEvent(String var1, int port) {
        this.ip = var1;
        this.port = port;
    }
}
