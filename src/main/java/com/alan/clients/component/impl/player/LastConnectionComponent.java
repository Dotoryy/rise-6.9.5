package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;

public final class LastConnectionComponent extends Component {
    public static String ip;
    public static int port;
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var0 -> {
        ip = var0.getIp();
        port = var0.getPort();
    };

    public LastConnectionComponent() {
    }
}
