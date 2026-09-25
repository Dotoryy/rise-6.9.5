package com.alan.clients.util.packet;

import net.minecraft.network.Packet;

public class QueuedPacket {
    public final Packet<?> tr;
    public final int ts;

    public QueuedPacket(Packet<?> packet, int var2) {
        this.tr = packet;
        this.ts = var2;
    }
}
