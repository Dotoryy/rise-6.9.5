package com.alan.clients.component.impl.packetlog;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;

public class PacketLogComponent extends Component {
    private int worldChanges;
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.worldChanges++;
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var1 -> this.worldChanges = 0;

    public PacketLogComponent() {
    }

    public boolean hasChangedWorlds() {
        return this.worldChanges > 0;
    }
}
