package com.alan.clients.newevent.impl.other;

import com.alan.clients.newevent.Event;
import java.util.List;
import lombok.Generated;

public final class ServerKickEvent implements Event {
    public List<String> message;

    @Generated
    public List<String> getMessage() {
        return this.message;
    }

    @Generated
    public ServerKickEvent(List<String> message) {
        this.message = message;
    }
}
