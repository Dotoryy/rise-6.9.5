package com.alan.clients.component.impl.player;

import com.alan.clients.util.packet.TimedPacket;
import java.util.Set;
import net.minecraft.network.Packet;

public final class BlinkPacket extends TimedPacket {
    private final Set<BlinkTicket> eu;

    public BlinkPacket(Packet<?> var1, Set<BlinkTicket> var2) {
        super(var1);
        this.eu = var2;
    }

    public boolean a(BlinkTicket var1) {
        return this.eu.remove(var1);
    }

    public void c(long var1) {
        this.eu.removeIf(var3 -> var3.a(this.getTime(), var1));
    }

    public boolean b(BlinkTicket var1) {
        return this.eu.contains(var1);
    }

    public boolean bt() {
        return this.eu.stream().anyMatch(var0 -> ((BlinkTicket)var0).bd());
    }
}
